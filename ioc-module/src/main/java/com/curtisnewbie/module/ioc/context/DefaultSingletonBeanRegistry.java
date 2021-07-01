package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.exceptions.*;
import com.curtisnewbie.module.ioc.util.ClassLoaderHolder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.curtisnewbie.module.ioc.util.BeanNameUtil.toBeanName;

/**
 * Implementation of {@link SingletonBeanRegistry}
 *
 * @author yongjie.zhuang
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

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

    private final BeanInstantiationStrategy beanInstantiationStrategy = new DefaultConstructorInstantiationStrategy();

    /** Indicate whether this registry is initialized, registry can only be initialised for once */
    private boolean isInitialised = false;

    public DefaultSingletonBeanRegistry() {
        this.beanClzScanner = new AnnotatedBeanClassScanner();
        this.beanDependencyParser = new AnnotatedBeanDependencyParser();
    }

    @Override
    public void registerSingletonBean(String beanName, Object bean) {
        synchronized (getMutex()) {
            if (beanInstanceMap.get(beanName) != null) {
                throw new SingletonBeanRegisteredException(beanName);
            }
            beanResolved.add(beanName);
            beanInstanceMap.put(beanName, bean);
            beanNameSet.add(beanName);
        }
    }

    @Override
    public void registerSingletonBean(Class<?> clazz, Object bean) {
        String beanName = toBeanName(clazz);
        registerSingletonBean(beanName, bean);
    }

    @Override
    public void registerDependency(String beanName, String dependentBeanName) {
        synchronized (this.dependenciesMap) {
            this.dependenciesMap.computeIfAbsent(beanName, k -> new HashSet<>());
            this.dependenciesMap.get(beanName).add(dependentBeanName);
        }
    }

    @Override
    public boolean isDependent(String beanName, String dependentBeanName) {
        synchronized (this.dependenciesMap) {
            return this.isDependentRecursive(beanName, dependentBeanName, new HashSet<>());
        }
    }

    /**
     * Recursively check if the other bean is a dependent bean
     *
     * @param beanName          current bean
     * @param dependentBeanName the other bean that current bean might be dependent on
     * @param beansSeen         the beans that we have seen
     */
    private boolean isDependentRecursive(String beanName, String dependentBeanName, Set<String> beansSeen) {
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
            if (isDependentRecursive(d, dependentBeanName, beansSeen)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsBean(String name) {
        return beanInstanceMap.containsKey(name);
    }

    @Override
    public boolean containsBean(Class<?> clazz) {
        return beanInstanceMap.containsKey(findNameOfPossiblyBeanAlias(toBeanName(clazz)));
    }

    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        return clazz.cast(getBeanByName(toBeanName(clazz)));
    }

    public Object getBeanByName(String beanName) {
        return beanInstanceMap.get(findNameOfPossiblyBeanAlias(beanName));
    }

    @Override
    public void loadBeanRegistry() {

        // TODO: 01/07/2021 how to inject the context-related beans in an extensible way

        synchronized (getMutex()) {

            // can only be initialised once
            if (isInitialised)
                throw new ContextInitializedException("Bean registry cannot be initialized multiple times");
            isInitialised = true;

            // set the classloader to use
            beanClzScanner.setClassLoader(classLoader);

            // prepare the registry before starting to populate beans and inject dependencies
            prepareBeanRegistry();

            // set of classes of beans that will be managed by this context
            Set<Class<?>> managedBeanClasses = beanClzScanner.scanBeanClasses();
            for (Class<?> c : managedBeanClasses) {
                if (c.isInterface()) {
                    throw new TypeNotSupportedForInjectionException(
                            String.format("Interface cannot be created as a managed bean (e.g., using %s), type: %s",
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

            // load dependencies of each bean recursively
            for (String beanName : beanNameSet) {
                resolveBeanRecursive(beanName);
            }

            for (BeanPostProcessor p : this.beanPostProcessors) {
                for (Map.Entry<String, Object> bean : this.beanInstanceMap.entrySet()) {
                    p.postProcessBean(bean.getValue(), bean.getKey());
                }
            }
        }
    }

    private void prepareBeanRegistry() {
        /*
        register itself as resolved dependency that can be later than be injected into other beans,
        but only the BeanRegistry interface can be used for dependency injection
         */
        this.registerSingletonBean(BeanRegistry.class, this);
    }

    @Override
    public void registerBeanPostProcessor(List<BeanPostProcessor> beanPostProcessorList) {
        synchronized (getMutex()) {
            this.beanPostProcessors.addAll(beanPostProcessorList);
        }
    }

    private void resolveBeanRecursive(String beanName) {
        String implBeanName = findNameOfPossiblyBeanAlias(beanName);

        // bean has been resolved
        if (beanResolved.contains(implBeanName))
            return;

        Class<?> beanClz = beanTypeMap.get(implBeanName);
        Objects.requireNonNull(beanClz, "Unable to find class of bean: " + beanName);

        Map<String, List<PropertyInfo>> dependencies = beanDependencyParser.parseDependenciesOfClass(beanClz);
        for (String dependentAlias : dependencies.keySet()) {
            String dependentImplBeanName = findNameOfPossiblyBeanAlias(dependentAlias);

            // detect unresolvable dependency
            if (!beanNameSet.contains(dependentImplBeanName)) {
                throw new UnsatisfiedDependencyException("Detected unresolvable dependency: " + dependentAlias);
            }
            // detect circular dependency
            if (isDependent(beanName, dependentAlias)) {
                throw new CircularDependencyException("Detected circular dependency between " + beanName + " and " + dependentAlias);
            }
            // update dependency cache
            registerDependency(beanName, dependentAlias);
            // continue to resolve the dependent bean
            resolveBeanRecursive(dependentAlias);
        }

        /* start to inject the dependencies between beans:
        1. instantiate the bean
        2. inject dependencies
         */
        // instantiate the bean
        Object bean = beanInstantiationStrategy.instantiateBean(beanTypeMap.get(implBeanName));
        registerSingletonBean(implBeanName, bean);

        // inject dependencies
        for (Map.Entry<String, List<PropertyInfo>> dependent : dependencies.entrySet()) {
            injectDependencies(bean, dependent.getKey(), dependent.getValue());
        }
    }

    private String findNameOfPossiblyBeanAlias(String beanAlias) {
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
            throw new CircularDependencyException(String.format("Found two beans (%s) with the same alias (%s)",
                    actualBeanNames.toString(), beanAlias));
        // the actual bean name is found, return it's type
        return actualBeanNames.iterator().next();
    }

    private Class<?> findTypeOfPossiblyBeanAlias(String beanAlias) {
        if (beanAlias == null)
            return null;
        String beanName = findNameOfPossiblyBeanAlias(beanAlias);
        if (beanName == null)
            return null;
        return beanTypeMap.get(beanName);
    }

    /**
     * Inject dependencies into the target bean
     *
     * @param bean                   bean
     * @param dependentBeanName      dependent bean's name (which might be an alias)
     * @param toBeInjectedProperties the properties info of this bean that require dependency injection
     */
    private void injectDependencies(Object bean, String dependentBeanName, List<PropertyInfo> toBeInjectedProperties) {
        Objects.requireNonNull(bean, "Unable to inject dependencies, bean is null");
        Objects.requireNonNull(dependentBeanName, "Unable to inject dependencies, dependent bean's name is null");

        if (toBeInjectedProperties == null || toBeInjectedProperties.isEmpty())
            return;

        // the actual implementation bean, the required type might be an interface, so we need to handle the casting
        String dependentImplBeanName = findNameOfPossiblyBeanAlias(dependentBeanName);
        Object dependentImplBeanInstance = beanInstanceMap.get(dependentImplBeanName);
        Objects.requireNonNull(dependentImplBeanInstance, "Unable to find instance of bean: " + dependentImplBeanName);

        for (PropertyInfo prop : toBeInjectedProperties) {
            Class<?> requiredType = prop.getPropertyBeanType();
            if (requiredType.isAssignableFrom(dependentImplBeanInstance.getClass())) {
                Method writeMethod = prop.getPropertyDescriptor().getWriteMethod();
                try {
                    // inject the dependent bean into the field
                    writeMethod.setAccessible(true);
                    writeMethod.invoke(bean, requiredType.cast(dependentImplBeanInstance));
                } catch (ReflectiveOperationException e) {
                    throw new UnableToInjectDependencyException("Unable to inject dependency in field: " + prop.getPropertyName());
                }
            }
        }
    }

    void setBeanClassScanner(BeanClassScanner beanClassScanner) {
        Objects.requireNonNull(beanClassScanner);
        synchronized (getMutex()) {
            this.beanClzScanner = beanClassScanner;
        }
    }

    void setBeanDependencyParser(BeanDependencyParser beanDependencyParser) {
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
