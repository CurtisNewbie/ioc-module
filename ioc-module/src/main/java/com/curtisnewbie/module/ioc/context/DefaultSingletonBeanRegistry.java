package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.beans.BeanPropertyInfo;
import com.curtisnewbie.module.ioc.processing.*;
import com.curtisnewbie.module.ioc.exceptions.*;
import com.curtisnewbie.module.ioc.util.ClassLoaderHolder;
import com.curtisnewbie.module.ioc.util.LogUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static com.curtisnewbie.module.ioc.util.BeanNameUtil.toBeanName;
import static com.curtisnewbie.module.ioc.util.LogUtil.info;
import static java.lang.String.format;

/**
 * Implementation of {@link SingletonBeanRegistry}
 *
 * @author yongjie.zhuang
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private static final Logger logger = LogUtil.getLogger(DefaultSingletonBeanRegistry.class);

    /**
     * Set of beans' name (excluding aliases)
     * <br>
     * This set will only include those (the implementation beans) that are managed by this registry, their interfaces
     * will not be included here.
     *
     * @see #beanAliasMap
     * @see #beanTypeMap
     */
    private final Set<String> beanNameSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Bean type map; bean name to bean type (including interfaces and aliases' types if any)
     * <br>
     * This map is used to track the class of each bean name, if class A implements interface B and C, then this map
     * will contains three pairs of name-class for this same singleton instance.
     * <br>
     * e.g.,
     *
     * <ol>
     *  <li>name for class A -> class A</li>
     *  <li>name for class B -> class B</li>
     *  <li>name for class C -> class C</li>
     * </ol>
     */
    private final Map<String, Class<?>> beanTypeMap = new ConcurrentHashMap<>();

    /**
     * Beans map; bean name to bean instance
     * <p>
     * If the bean has multiple interfaces, the bean name will always be the name of the implementation bean, the
     * interface names will be the bean's alias
     *
     * @see #beanAliasMap
     */
    private final Map<String, Object> beanInstanceMap = new ConcurrentHashMap<>();

    /**
     * Dependencies map; bean name to its dependencies
     * <p>
     * Dependencies will be the name of the required bean. E.g,. if bean A depends on bean B, but the B is an interface,
     * and its actual implementation is type C, then the dependent bean name will still be B.
     */
    private final Map<String, Set<String>> dependenciesMap = new ConcurrentHashMap<>();

    /**
     * Alias map; bean alias (e.g., interfaces) to a set of actual bean names
     * <br>
     */
    private final Map<String, Set<String>> beanAliasMap = new ConcurrentHashMap<>();

    /** Set of bean's name; where in the bean is resolved already, including its dependencies */
    private final Set<String> beanResolved = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /** mutex lock */
    private final Object mutex = new Object();

    /** Scanner of classes of annotated beans */
    private BeanClassScanner beanClzScanner;

    /** Parser of beans' dependencies */
    private BeanDependencyParser beanDependencyParser;

    /** List of BeanPostProcessors that process the bean after instantiation */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final ClassLoader classLoader = ClassLoaderHolder.getClassLoader();

    private BeanInstantiationStrategy beanInstantiationStrategy = new DefaultConstructorInstantiationStrategy();

    /** Indicate whether this registry is initialized, registry can only be initialised for once */
    private boolean isInitialised = false;

    public DefaultSingletonBeanRegistry() {
        this.beanClzScanner = new AnnotatedBeanClassScanner();
        this.beanDependencyParser = new AnnotatedBeanDependencyParser();
    }

    @Override
    public void registerSingletonBean(String beanName, Object bean) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(bean);

        synchronized (getMutex()) {
            if (beanInstanceMap.get(beanName) != null) {
                throw new SingletonBeanRegisteredException(beanName);
            }
            beanResolved.add(beanName);
            beanInstanceMap.put(beanName, bean);
            beanNameSet.add(beanName);
        }
    }

    /** Register eagerly created singleton bean, whose dependencies are not yet resolved */
    @Override
    public void registerEagerlyCreatedSingletonBean(String beanName, Object bean) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(bean);

        synchronized (getMutex()) {
            if (beanInstanceMap.get(beanName) != null) {
                throw new SingletonBeanRegisteredException(beanName);
            }
            beanInstanceMap.put(beanName, bean);
            beanNameSet.add(beanName);
        }
    }

    @Override
    public void registerSingletonBean(Class<?> clazz, Object bean) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(bean);

        String beanName = toBeanName(clazz);
        registerSingletonBean(beanName, bean);
    }

    @Override
    public void registerDependency(String beanName, String dependentBeanName) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(dependentBeanName);

        synchronized (this.dependenciesMap) {
            this.dependenciesMap.computeIfAbsent(beanName, k -> new HashSet<>());
            this.dependenciesMap.get(beanName).add(dependentBeanName);
        }
    }

    @Override
    public boolean isDependent(String beanName, String dependentBeanName) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(dependentBeanName);

        synchronized (this.dependenciesMap) {
            return this.checkIsDependentRecursively(beanName, dependentBeanName, new HashSet<>());
        }
    }

    @Override
    public boolean isBeanResolved(String beanName) {
        String implBeanName = findNameOfPossibleBeanAlias(beanName);
        return beanResolved.contains(implBeanName);
    }

    @Override
    public void markBeanAsResolved(String beanName) {
        String implBeanName = findNameOfPossibleBeanAlias(beanName);
        if (!beanResolved.add(implBeanName)) {
            // ensure bugs are discovered as early as possible
            throw new IllegalArgumentException(
                    format("Bean: '%s' has been resolved already, cannot be marked again", beanName)
            );
        }
    }

    /**
     * Recursively check if the other bean is a dependent bean
     *
     * @param beanName          current bean
     * @param dependentBeanName the other bean that current bean might be dependent on
     * @param beansSeen         the beans that we have seen
     */
    private boolean checkIsDependentRecursively(String beanName, String dependentBeanName, Set<String> beansSeen) {
        // circular dependency, not dependent
        if (beansSeen.contains(beanName)) {
            return false;
        }
        // not found, even in a recursive way
        Set<String> dependentBeans = this.dependenciesMap.get(beanName);
        if (dependentBeans == null || dependentBeans.isEmpty())
            return false;
        if (dependentBeans.contains(dependentBeanName)) {
            return true;
        }
        // check transitive dependencies
        for (String d : dependentBeans) {
            beansSeen.add(d);
            if (checkIsDependentRecursively(d, dependentBeanName, beansSeen)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsBean(String name) {
        Objects.requireNonNull(name);
        return beanInstanceMap.containsKey(name);
    }

    @Override
    public boolean containsBean(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return beanInstanceMap.containsKey(findNameOfPossibleBeanAlias(toBeanName(clazz)));
    }

    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.cast(getBeanByName(toBeanName(clazz)));
    }

    public Object getBeanByName(String beanName) {
        Objects.requireNonNull(beanName);
        return beanInstanceMap.get(findNameOfPossibleBeanAlias(beanName));
    }

    @Override
    public void loadBeanRegistry() {

        synchronized (getMutex()) {

            // can only be initialised once
            if (isInitialised)
                throw new ContextInitializedException("Bean registry cannot be initialized multiple times");
            isInitialised = true;

            info(logger, "Starts loading bean registry");

            // set the classloader to use
            beanClzScanner.setClassLoader(classLoader);

            // prepare the registry before starting to populate beans and inject dependencies
            prepareBeanRegistry();
            info(logger, "Bean registry prepared");

            // set of classes of beans that will be managed by this context
            Set<Class<?>> managedBeanClasses = beanClzScanner.scanBeanClasses();
            info(logger, "Discovered beans: %d", managedBeanClasses.size());

            // register these managed beans, including their interfaces as aliases
            registerManagedBeans(managedBeanClasses);
            info(logger, "Beans registered");

            // instantiate all the beans first (without any references of dependencies being injected)
            for (String beanName : beanNameSet) {
                instantiateUnresolvedBeanEagerly(beanName);
            }
            info(logger, "Beans instantiated eagerly");

            // todo this part should be moved to post processor
            // resolve dependencies between beans, but the dependencies are not yet injected
            for (String beanName : beanNameSet) {
                resolveDependenciesRecursively(beanName);
            }
            // todo ------

            // delegate actual dependency injection to the postProcessors for extensibility
            // and the post processing is applied after all managed beans' instantiation
            applyPostProcessing();
            info(logger, "Beans post processing applied");


        }
    }

    /**
     * Register a set of beans' classes that are found by the scanner
     * <br>
     * Registering, means loading these classes into the {@link #beanTypeMap}, {@link #beanNameSet}, {@link
     * #beanAliasMap}, this method doesn't involve populating the beans and injectiong their dependencies.
     * <br>
     * This method also collect their interfaces, and use them as aliases, such that we can find a bean (of the actual
     * concrete implementation) by one of the interface that it implements.
     *
     * @see #loadBeanRegistry()
     * @see #resolveDependenciesRecursively(String)
     * @see #applyPostProcessing()
     */
    private void registerManagedBeans(Set<Class<?>> managedBeanClasses) {
        for (Class<?> c : managedBeanClasses) {
            if (c.isInterface()) {
                throw new TypeNotSupportedForInjectionException(
                        format("Interface cannot be created as a managed bean (e.g., using %s), type: %s",
                                MBean.class.getSimpleName(), c.toString())
                );
            }
            String beanName = toBeanName(c);
            beanTypeMap.put(beanName, c);
            beanNameSet.add(beanName);

            // register the interfaces' name as this bean's alias
            Set<Class<?>> interfaces = new HashSet<>();

            // interfaces that will be collected
            Queue<Class<?>> toBeAddedInterfaces = new LinkedList<>(Arrays.asList(c.getInterfaces()));

            while (!toBeAddedInterfaces.isEmpty()) {
                Class<?> f = toBeAddedInterfaces.poll();

                // have seen this interface already
                if (interfaces.contains(f))
                    continue;

                interfaces.add(f);

                    /*
                    check if this interface has parent interfaces, if so, put them into the queue
                    */
                Class<?>[] parents = f.getInterfaces();
                if (parents != null && parents.length > 0) {
                    toBeAddedInterfaces.addAll(Arrays.asList(parents));
                }
            }

            for (Class<?> ic : interfaces) {
                    /*
                    if the alias map already had this interface,
                    this means that two or more beans are sharing the same interface, but
                    it doesn't necessary cause circular dependency, as long as the different interfaces
                    are used for injection
                     */
                String interfaceName = toBeanName(ic);
                beanTypeMap.put(interfaceName, ic);
                beanAliasMap.computeIfAbsent(interfaceName, k -> new HashSet<>());
                beanAliasMap.get(interfaceName).add(beanName);
            }
        }
    }

    /** Prepare the bean registry before starting any actual bean scanning, bean instantiation, and dependency injection */
    private void prepareBeanRegistry() {
        /*
        register itself as resolved dependency that can be later than be injected into other beans,
        but only the BeanRegistry interface can be used for dependency injection
         */
        this.registerSingletonBean(BeanRegistry.class, this);
    }

    /**
     * Instantiate managed beans & inject dependencies
     * <p>
     * All beans are instantiated first, then the dependencies between them are injected
     * </p>
     *
     * @see #instantiateUnresolvedBeanEagerly(String)
     * @see #resolveDependenciesRecursively(String)
     */
    private void resolveBeans() {

    }

    /**
     * Instantiate the bean eagerly using pre-selected {@link BeanInstantiationStrategy}
     */
    private void instantiateUnresolvedBeanEagerly(String beanName) {
        String implBeanName = findNameOfPossibleBeanAlias(beanName);
        if (!beanResolved.contains(implBeanName)) {
            Object bean = beanInstantiationStrategy.instantiateBean(beanTypeMap.get(implBeanName));
            // register an eagerly created bean, wherein the dependencies are not resolved
            registerEagerlyCreatedSingletonBean(implBeanName, bean);
        }
    }

    private void applyPostProcessing() {
        for (BeanPostProcessor p : this.beanPostProcessors) {
            for (Map.Entry<String, Object> bean : this.beanInstanceMap.entrySet()) {
                Object processedObj = p.postProcessBean(bean.getValue(), bean.getKey());
                Objects.requireNonNull(processedObj, "BeanPostProcessor " + p.getClass().getSimpleName()
                        + " should not return null object after processing");
                this.beanInstanceMap.replace(bean.getKey(), processedObj);
            }
        }
    }

    @Override
    public void registerBeanPostProcessor(List<BeanPostProcessor> beanPostProcessorList) {
        Objects.requireNonNull(beanPostProcessorList);
        if (!beanPostProcessorList.isEmpty()) {
            synchronized (getMutex()) {
                this.beanPostProcessors.addAll(beanPostProcessorList);
            }
        }
    }

    // TODO: 02/07/2021 Move this part to post processors

    /**
     * Resolve dependencies between beans (in beanNameSet) recursively
     * <p>
     * The child nodes are resolved first, then the parent nodes. Eventually, there will be a child node that doesn't
     * have any dependency, so we can just simply create it, and inject it into its 'parent' bean, this is essentially
     * how it works.
     * </p>
     * <p>
     * The dependencies between beans are just like trees (or, strictly speaking, a graph). A graph without circles
     * (circular dependencies) is essentially a n-node tree.
     * </p>
     */
    private void resolveDependenciesRecursively(String beanName) {
        Objects.requireNonNull(beanName);

        // bean has been resolved
        if (isBeanResolved(beanName))
            return;

        // get the instantiated bean, see if it's actually populated
        Object bean = getBeanByName(beanName);
        Objects.requireNonNull(bean, format("Bean: '%s' has not yet been instantiated, unable to inject dependencies",
                beanName));

        Class<?> beanClz = bean.getClass();
        Map<String, List<BeanPropertyInfo>> dependencies = beanDependencyParser.parseDependenciesOfClass(beanClz);
        for (String dependentAlias : dependencies.keySet()) {
            String dependentImplBeanName = findNameOfPossibleBeanAlias(dependentAlias);

            // detect unresolvable dependency
            if (!beanInstanceMap.containsKey(dependentImplBeanName)) {
                throw new UnsatisfiedDependencyException("Detected unresolvable dependency: " + dependentAlias);
            }
            // detect circular dependency
            if (isDependent(beanName, dependentAlias)) {
                throw new CircularDependencyException("Detected circular dependency between " + beanName + " and " + dependentAlias);
            }
            // update dependency cache
            registerDependency(beanName, dependentAlias);
            // continue to resolve the dependent bean
            resolveDependenciesRecursively(dependentAlias);
        }

        // inject dependencies
        for (Map.Entry<String, List<BeanPropertyInfo>> dependent : dependencies.entrySet()) {
            injectDependencies(bean, dependent.getKey(), dependent.getValue());
        }

        // TODO: 02/07/2021 if we use postProcessors for extensibility, and there are multiple postProcessors that
        //  inject dependencies for different annotations, how do we know which bean is actually resolved? Since we are
        //  traversing a graph, how do we prevent parsing the dependency tree of a previous traversed bean again and again.
        //  The 'mark been as resolved' idea doesn't seem right :(.
        // mark the bean as resolved
        markBeanAsResolved(beanName);
    }

    /** Find actual implementation bean's name by a possible alias */
    private String findNameOfPossibleBeanAlias(String beanAlias) {
        Objects.requireNonNull(beanAlias);

        // first check if this beanName is actually an alias
        if (beanNameSet.contains(beanAlias)) {
            return beanAlias;
        }

        // this bean name is an alias, see if it's pointing to some bean
        Set<String> actualBeanNames = beanAliasMap.get(beanAlias);
        if (actualBeanNames == null || actualBeanNames.isEmpty())
            return null;
        // multiple beans are found, must have circular dependencies
        if (actualBeanNames.size() > 1)
            throw new CircularDependencyException(format("Found two beans (%s) with the same alias (%s)",
                    actualBeanNames.toString(), beanAlias));
        // the actual bean name is found, return it's type
        return actualBeanNames.iterator().next();
    }

    // TODO: 02/07/2021 Move this part to post processors

    /**
     * Inject dependencies into the target bean
     *
     * @param bean                   bean
     * @param dependentBeanName      dependent bean's name (which might be an alias)
     * @param toBeInjectedProperties the properties info of this bean that require dependency injection
     */
    private void injectDependencies(Object bean, String dependentBeanName, List<BeanPropertyInfo> toBeInjectedProperties) {
        Objects.requireNonNull(bean, "Unable to inject dependencies, bean is null");
        Objects.requireNonNull(dependentBeanName, "Unable to inject dependencies, dependent bean's name is null");
        Objects.requireNonNull(toBeInjectedProperties, "Unable to inject dependencies, properties list to be injected is null");

        if (toBeInjectedProperties == null || toBeInjectedProperties.isEmpty())
            return;

        // the actual implementation bean, the required type might be an interface, so we need to handle the casting
        Object dependentImplBeanInstance = getBeanByName(dependentBeanName);
        Objects.requireNonNull(dependentImplBeanInstance, "Unable to find instance of bean: " + dependentBeanName);

        for (BeanPropertyInfo prop : toBeInjectedProperties) {
            if (prop.canSatisfyRequiredType(dependentImplBeanInstance.getClass())) {
                // inject the dependent bean into the field
                prop.setValueToPropertyOfBean(bean, dependentImplBeanInstance);
            }
        }
    }

    /** Set {@link BeanClassScanner} to be used by the registry, should invoke this before {@link #loadBeanRegistry()} */
    @Override
    public void setBeanClassScanner(BeanClassScanner beanClassScanner) {
        Objects.requireNonNull(beanClassScanner);
        synchronized (getMutex()) {
            this.beanClzScanner = beanClassScanner;
        }
    }

    @Override
    public void setBeanInstantiationStrategy(BeanInstantiationStrategy beanInstantiationStrategy) {
        Objects.requireNonNull(beanInstantiationStrategy);
        synchronized (getMutex()) {
            this.beanInstantiationStrategy = beanInstantiationStrategy;
        }
    }

    /**
     * Set {@link BeanDependencyParser} to be used by the registry, should invoke this before {@link
     * #loadBeanRegistry()}
     */
    @Override
    public void setBeanDependencyParser(BeanDependencyParser beanDependencyParser) {
        Objects.requireNonNull(beanDependencyParser);
        synchronized (getMutex()) {
            this.beanDependencyParser = beanDependencyParser;
        }
    }

    /** Get object for mutex lock */
    private Object getMutex() {
        return this.mutex;
    }
}
