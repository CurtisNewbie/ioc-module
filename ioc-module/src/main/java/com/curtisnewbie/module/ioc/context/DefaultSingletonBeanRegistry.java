package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.exceptions.*;
import com.curtisnewbie.module.ioc.util.ClassLoaderHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.curtisnewbie.module.ioc.util.BeanNameUtil.toBeanName;

// TODO: this implementation is not working for beans that implement and act as different interfaces

/**
 * Implementation of {@link SingletonBeanRegistry}
 *
 * @author yongjie.zhuang
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /** A set of beans' names (a set that is backed by a concurrentHashMap) */
    private final Set<String> beanNameSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
    /** Bean type map; bean name to bean type */
    private final Map<String, Class<?>> beanTypeMap = new ConcurrentHashMap<>();
    /** Beans map; bean name to bean instance */
    private final Map<String, Object> beanInstanceMap = new ConcurrentHashMap<>();
    /** Dependencies map; bean name to its dependencies */
    private final Map<String, Set<String>> dependenciesMap = new ConcurrentHashMap<>();
    /** Set of bean's name; where in the bean is resolved already, including its dependencies */
    private final Set<String> beanResolved = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /** mutex lock */
    private final Object mutex = new Object();

    /** Scanner of classes of annotated beans */
    private final BeanClassScanner beanClzScanner;
    /** Resolver of beans' dependencies */
    private final BeanDependencyResolver dependencyResolver;
    /** List of BeanPostProcessors that process the bean after instantiation */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final ClassLoader classLoader = ClassLoaderHolder.getClassLoader();

    /** Indicate whether this registry is initialized */
    private boolean isInitialise = false;

    public DefaultSingletonBeanRegistry() {
        this.beanClzScanner = new AnnotatedBeanClassScanner();
        this.dependencyResolver = new AnnotatedBeanDependencyResolver();
    }

    @Override
    public void registerSingletonBean(String beanName, Object bean) {
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
        return beanNameSet.contains(name);
    }

    @Override
    public boolean containsBean(Class<?> clazz) {
        return beanNameSet.contains(toBeanName(clazz));
    }

    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        return clazz.cast(getBeanByName(toBeanName(clazz)));
    }

    public Object getBeanByName(String beanName) {
        return beanInstanceMap.get(beanName);
    }

    @Override
    public void loadBeanRegistry() {

        synchronized (getMutex()) {

            // can only be initialised once
            if (isInitialise)
                throw new ContextInitializedException("Bean registry cannot be initialized multiple times");
            isInitialise = true;

            Objects.requireNonNull(classLoader);

            // set of classes of beans that will be managed by this context
            Set<Class<?>> managedBeanClasses = beanClzScanner.scanBeanClasses(classLoader);
            for (Class<?> c : managedBeanClasses) {
                if (c.isInterface()) {
                    throw new TypeNotSupportedForInjectionException("Interface cannot be injected, type: " + c.toString());
                }
                String beanName = toBeanName(c);
                beanTypeMap.put(beanName, c);
                beanNameSet.add(beanName);
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

    @Override
    public void registerBeanPostProcessor(List<BeanPostProcessor> beanPostProcessorList) {
        synchronized (getMutex()) {
            this.beanPostProcessors.addAll(beanPostProcessorList);
        }
    }

    private void resolveBeanRecursive(String beanName) {
        // bean has been resolved
        if (beanResolved.contains(beanName))
            return;

        Class<?> beanClz = beanTypeMap.get(beanName);
        Objects.requireNonNull(beanClz, "Unable to find class of bean: " + beanName);

        Map<String, List<PropertyInfo>> dependencies = dependencyResolver.resolveDependenciesOfClass(beanClz);
        for (String dependent : dependencies.keySet()) {
            // detect unresolvable dependency
            if (!beanTypeMap.containsKey(dependent)) {
                throw new UnsatisfiedDependencyException("Detected unresolvable dependency: " + dependent);
            }
            // detect circular dependency
            if (isDependent(beanName, dependent)) {
                throw new CircularDependencyException("Detected circular dependency between " + beanName + " and " + dependent);
            }
            // update dependency cache
            registerDependency(beanName, dependent);
            // continue to resolve the dependent bean
            resolveBeanRecursive(dependent);
        }

        // start to inject the dependencies between beans
        // instantiate the bean
        Object bean = instantiateBean(beanName);
        registerSingletonBean(beanName, bean);
        // inject dependencies
        for (Map.Entry<String, List<PropertyInfo>> dependent : dependencies.entrySet()) {
            injectDependencies(bean, beanName, dependent.getKey(), dependent.getValue());
        }
        beanResolved.add(beanName);
    }

    /** Instantiate bean with default constructor */
    private Object instantiateBean(String beanName) {
        Class<?> clz = beanTypeMap.get(beanName);
        Objects.requireNonNull(clz, "Unable to find Class of bean: " + beanName);
        // create bean with default constructor
        try {
            Constructor<?> defConstructor = clz.getDeclaredConstructor();
            Objects.requireNonNull(defConstructor, "Unable to create bean: " + beanName + " using default constructor");
            return defConstructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new BeanCreationException("Unable to create bean: " + beanName + " using default constructor", e);
        }
    }

    /**
     * Inject dependencies into the target bean
     *
     * @param bean                   bean
     * @param beanName               name of the bean
     * @param dependentBeanName      dependent bean's name
     * @param toBeInjectedProperties the properties info of this bean that require dependency injection
     */
    private void injectDependencies(Object bean, String beanName, String dependentBeanName, List<PropertyInfo> toBeInjectedProperties) {
        Class<?> dependencyClz = beanTypeMap.get(dependentBeanName);
        Objects.requireNonNull(dependencyClz, "Unable to find Class of bean: " + dependentBeanName);

        for (PropertyInfo prop : toBeInjectedProperties) {
            // the property's type matches the dependency type
            if (prop.getPropertyBeanType().equals(dependencyClz)) {
                Method writeMethod = prop.getPropertyDescriptor().getWriteMethod();
                // inject the dependent bean into the field
                try {
                    writeMethod.setAccessible(true);
                    writeMethod.invoke(bean, beanInstanceMap.get(dependentBeanName));
                } catch (ReflectiveOperationException e) {
                    throw new UnableToInjectDependencyException("Unable to inject dependency in bean: " + beanName
                            + ", field: " + prop.getPropertyName());
                }
            }
        }
    }

    private Object getMutex() {
        return this.mutex;
    }
}
