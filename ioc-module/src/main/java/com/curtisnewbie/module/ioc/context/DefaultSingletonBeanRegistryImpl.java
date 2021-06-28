package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.exceptions.SingletonBeanRegistered;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.curtisnewbie.module.ioc.util.BeanNameUtil.toBeanName;

/**
 * Implementation of {@link SingletonBeanRegistry}
 *
 * @author yongjie.zhuang
 */
public class DefaultSingletonBeanRegistryImpl implements SingletonBeanRegistry {

    /** A set of beans' names (a set that is backed by a concurrentHashMap) */
    private final Set<String> beanNameSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
    /** Bean type map; bean name to bean type */
    private final Map<String, Class<?>> beanTypeMap = new ConcurrentHashMap<>();
    /** Beans map; bean name to bean instance */
    private final Map<String, Object> beanInstanceMap = new ConcurrentHashMap<>();
    /** Dependencies map; bean name to its dependencies */
    private final Map<String, Set<String>> dependenciesMap = new ConcurrentHashMap<>();
    /** mutex lock */
    private final Object mutex = new Object();

    /** Scanner of classes of annotated beans */
    private final BeanClassScanner beanClzScanner;
    /** Resolver of beans' dependencies */
    private final BeanDependencyResolver dependencyResolver;

    private final ClassLoader classLoader;
    /** Indicate whether this registry is initialized */
    private boolean isInitialise = false;

    public DefaultSingletonBeanRegistryImpl(ClassLoader classLoader) {
        this.beanClzScanner = new AnnotatedBeanClassScanner();
        this.dependencyResolver = new AnnotatedBeanDependencyResolver();

        if (classLoader != null) {
            this.classLoader = classLoader;
        } else {
            this.classLoader = this.getClass().getClassLoader();
        }
    }

    public DefaultSingletonBeanRegistryImpl() {
        this(null);
    }

    @Override
    public void registerSingletonBean(String beanName, Object bean) throws SingletonBeanRegistered {
        synchronized (mutex) {
            if (beanInstanceMap.get(beanName) != null) {
                throw new SingletonBeanRegistered(beanName + " has been registered");
            }
            beanInstanceMap.put(beanName, bean);
            beanNameSet.add(beanName);
        }
    }

    @Override
    public void registerSingletonBean(Class<?> clazz, Object bean) throws SingletonBeanRegistered {
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
        // can only be initialised once
        synchronized (mutex) {
            if (isInitialise)
                throw new IllegalStateException("Bean registry cannot be initialized multiple times");
            isInitialise = true;
        }
        // set of classes of beans that will be managed by this context
        Set<Class<?>> managedBeanClasses = beanClzScanner.scanBeanClasses(classLoader);

        for (Class<?> c : managedBeanClasses) {
            if (c.isInterface()) {
                throw new IllegalStateException("Interface cannot be injected, type: " + c.toString());
            }
            String beanName = toBeanName(c);
            beanTypeMap.put(beanName, c);
            beanNameSet.add(beanName);
        }

        // load dependencies of each bean recursively
        for (String beanName : beanNameSet) {
            resolveBeanRecursive(beanName);
        }
    }

    private void resolveBeanRecursive(String beanName) {
        Class<?> beanClz = beanTypeMap.get(beanName);
        Objects.requireNonNull(beanClz, "Unable to find class of bean: " + beanName);

        Map<String, List<PropertyInfo>> dependencies = dependencyResolver.resolveDependenciesOfClass(beanClz);
        for (String dependent : dependencies.keySet()) {
            // detect unresolvable dependency
            if (!beanTypeMap.containsKey(dependent)) {
                throw new IllegalStateException("Detected unresolvable dependency: " + dependent);
            }
            // detect circular dependency
            if (isDependent(beanName, dependent)) {
                throw new IllegalStateException("Detected circular dependency between " + beanName + " and " + dependent);
            }
            // update dependency cache
            registerDependency(beanName, dependent);
            // continue to resolve the dependent bean
            resolveBeanRecursive(dependent);
        }

        // start to inject the dependencies between beans
        // 1.instantiate a bean
        Object bean = instantiateBean(beanName);
        beanInstanceMap.put(beanName, bean);

        // 2.setup it's dependencies
        for (Map.Entry<String, List<PropertyInfo>> dependent : dependencies.entrySet()) {
            injectDependencies(bean, beanName, dependent.getKey(), dependent.getValue());
        }
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
            throw new IllegalStateException("Unable to create bean: " + beanName + " using default constructor", e);
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
                    throw new IllegalStateException("Unable to inject dependency in bean: " + beanName + ", field: " + prop.getPropertyName());
                }
            }
        }
    }
}
