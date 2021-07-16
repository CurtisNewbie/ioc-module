package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.processing.*;
import com.curtisnewbie.module.ioc.exceptions.*;
import com.curtisnewbie.module.ioc.util.ClassLoaderHolder;
import com.curtisnewbie.module.ioc.util.LogUtil;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.curtisnewbie.module.ioc.util.LogUtil.info;
import static java.lang.String.format;

/**
 * Implementation of {@link SingletonBeanRegistry}
 *
 * @author yongjie.zhuang
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private static final Logger logger = LogUtil.getLogger(DefaultSingletonBeanRegistry.class);
    private final AtomicBoolean isLogMuted = new AtomicBoolean(false);

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

    /** mutex lock */
    private final Object mutex = new Object();

    /** List of BeanPostProcessors that process the bean after instantiation */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final ClassLoader classLoader = ClassLoaderHolder.getClassLoader();

    /*
    Configurable components, which are also the potential extension points
     */
    private final BeanClassScanner beanClzScanner;
    private final BeanNameGenerator beanNameGenerator;
    private final BeanInstantiationStrategy beanInstantiationStrategy;
    private final BeanAliasParser beanAliasParser;

    /** Indicate whether this registry is initialized, registry can only be initialised for once */
    private boolean isInitialised = false;

    public DefaultSingletonBeanRegistry(
            BeanClassScanner beanClassScanner,
            BeanNameGenerator beanNameGenerator,
            BeanInstantiationStrategy beanInstantiationStrategy,
            BeanAliasParser beanAliasParser
    ) {
        this.beanClzScanner = beanClassScanner;
        this.beanNameGenerator = beanNameGenerator;
        this.beanInstantiationStrategy = beanInstantiationStrategy;
        this.beanAliasParser = beanAliasParser;
    }

    @Override
    public String getBeanName(String beanNameOrAlias) {
        String implBean = findNameOfPossibleBeanAlias(beanNameOrAlias);
        return implBean;
    }

    @Override
    public void registerSingletonBean(String beanName, Object bean) {
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

        String beanName = beanNameGenerator.generateBeanName(clazz);
        registerSingletonBean(beanName, bean);
    }

    @Override
    public Map<String, Object> getBeansOfType(Class<?> parentType) {
        Objects.requireNonNull(parentType);

        Map<String, Object> beanNameToObj = new HashMap<>();
        synchronized (getMutex()) {
            // if the requested type itself is a implementation bean
            String parentBeanName = beanNameGenerator.generateBeanName(parentType);
            Object parentDirectImpl = beanInstanceMap.get(parentBeanName);
            if (parentDirectImpl != null)
                beanNameToObj.put(parentBeanName, parentDirectImpl);

            // try to find if any other beans that extend or implement this type
            Set<String> childBeans = beanAliasMap.get(parentBeanName);
            if (childBeans != null) {
                for (String childName : childBeans) {
                    Object childObj = beanInstanceMap.get(childName);
                    Objects.requireNonNull(childObj);
                    if (childObj != parentDirectImpl) {
                        beanNameToObj.put(childName, childObj);
                    }
                }
            }
            return beanNameToObj;
        }
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
        String implBeanName = findNameOfPossibleBeanAlias(name);
        if (implBeanName == null)
            return false;
        return beanInstanceMap.containsKey(implBeanName);
    }

    @Override
    public boolean containsBean(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return containsBean(beanNameGenerator.generateBeanName(clazz));
    }

    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.cast(getBeanByName(beanNameGenerator.generateBeanName(clazz)));
    }

    @Override
    public Object getBeanByName(String beanName) {
        Objects.requireNonNull(beanName);
        String implBeanName = findNameOfPossibleBeanAlias(beanName);
        if (implBeanName == null)
            return null;
        return beanInstanceMap.get(implBeanName);
    }

    @Override
    public void loadBeanRegistry() {

        synchronized (getMutex()) {

            // can only be initialised once
            if (isInitialised)
                throw new ContextInitializedException("Bean registry cannot be initialized multiple times");
            isInitialised = true;

            logIfNotMuted("Starts loading bean registry");

            // set the classloader to use
            beanClzScanner.setClassLoader(classLoader);

            // prepare the registry before starting to populate beans and inject dependencies
            prepareBeanRegistry();
            logIfNotMuted("Bean registry prepared");

            // set of classes of beans that will be managed by this context
            Set<Class<?>> managedBeanClasses = beanClzScanner.scanBeanClasses();
            logIfNotMuted("Discovered beans: %d", managedBeanClasses.size());

            // register these managed beans, including their interfaces as aliases
            registerManagedBeans(managedBeanClasses);
            logIfNotMuted("Beans registered");

            // instantiate all the beans first (without any references of dependencies being injected)
            for (String beanName : beanNameSet) {
                instantiateUnresolvedBeanEagerly(beanName);
            }
            logIfNotMuted("Beans instantiated eagerly");

            // delegate actual dependency injection to the postProcessors for extensibility
            // and the post processing is applied after all managed beans' instantiation
            applyPostProcessing();
            logIfNotMuted("Beans post processing applied");
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
            String beanName = beanNameGenerator.generateBeanName(c);
            beanTypeMap.put(beanName, c);
            beanNameSet.add(beanName);

            // register the superClass and interfaces' name as this bean's alias
            Set<String> aliases = beanAliasParser.parseBeanAliases(c);

            /*
            if the map already contains this alias,
            this means that two or more beans are sharing the same interface, but
            it doesn't necessary cause circular dependency, as long as the different interfaces/superclasses
            are used for injection
             */
            for (String al : aliases) {
                beanAliasMap.computeIfAbsent(al, k -> new HashSet<>());
                beanAliasMap.get(al).add(beanName);
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
     * Instantiate the bean eagerly using pre-selected {@link BeanInstantiationStrategy}
     */
    private void instantiateUnresolvedBeanEagerly(String beanName) {
        String implBeanName = findNameOfPossibleBeanAlias(beanName);
        Objects.requireNonNull(implBeanName,
                format("Bean: '%s' not registered, cannot be instantiated", beanName));
        if (!beanInstanceMap.containsKey(implBeanName)) {
            Object bean = beanInstantiationStrategy.instantiateBean(beanTypeMap.get(implBeanName));
            // register an eagerly created bean, wherein the dependencies are not resolved
            registerEagerlyCreatedSingletonBean(implBeanName, bean);
        }
    }

    private void applyPostProcessing() {
        for (BeanPostProcessor p : this.beanPostProcessors) {
            for (Map.Entry<String, Object> bean : this.beanInstanceMap.entrySet()) {
                Object processedObj = p.postProcessBeanAfterInstantiation(bean.getValue(), bean.getKey());
                Objects.requireNonNull(processedObj, "BeanPostProcessor " + p.getClass().getSimpleName()
                        + " should not return null object after processing");
                this.beanInstanceMap.replace(bean.getKey(), processedObj);
            }
            logIfNotMuted("Applied BeanPostProcessor: '%s'", p.getClass().getName());
        }
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

    /** Get object for mutex lock */
    private Object getMutex() {
        return this.mutex;
    }

    @Override
    public boolean canMuteLog() {
        return true;
    }

    @Override
    public void muteLog() {
        isLogMuted.set(true);
    }

    @Override
    public boolean isLogMuted() {
        return isLogMuted.get();
    }

    private void logIfNotMuted(String formatStr, Object... args) {
        if (!isLogMuted())
            info(logger, formatStr, args);
    }

    @Override
    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Objects.requireNonNull(beanPostProcessor);
        synchronized (getMutex()) {
            this.beanPostProcessors.add(beanPostProcessor);
        }
    }
}
