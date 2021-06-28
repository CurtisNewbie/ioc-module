package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.exceptions.SingletonBeanRegistered;
import com.curtisnewbie.module.ioc.util.BeanNameUtil;

import java.beans.Introspector;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of {@link SingletonBeanRegistry}
 *
 * @author yongjie.zhuang
 */
public class DefaultSingletonBeanRegistryImpl implements SingletonBeanRegistry {

    /** A set of beans' names (a set that is backed by a concurrentHashMap) */
    private final Set<String> beanNameSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
    /** Beans map; bean name to bean instance */
    private final ConcurrentMap<String, Object> beanInstanceMap = new ConcurrentHashMap<>();
    /** Dependencies map; bean name to its dependencies */
    private final ConcurrentMap<String, Set<String>> dependenciesMap = new ConcurrentHashMap<>();
    /** mutex lock */
    private final Object mutex = new Object();

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
        String beanName = BeanNameUtil.toBeanName(clazz);
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
        return beanNameSet.contains(clazz.getName());
    }

    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        return clazz.cast(getBeanByName(clazz.getName()));
    }

    public Object getBeanByName(String beanName) {
        return beanInstanceMap.get(beanName);
    }
}
