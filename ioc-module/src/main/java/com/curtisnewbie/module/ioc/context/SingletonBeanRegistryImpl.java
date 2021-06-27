package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.exceptions.SingletonBeanRegistered;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of {@link SingletonBeanRegistry}
 *
 * @author yongjie.zhuang
 */
public class SingletonBeanRegistryImpl implements SingletonBeanRegistry {

    /** A set of injectable beans' names (a set that is backed by a concurrentHashMap) */
    private final Set<String> injectableBeanNameSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
    /** Injectable beans map; bean name to bean instance */
    private final ConcurrentMap<String, Object> injectableInstanceMap = new ConcurrentHashMap<>();
    /** mutex lock */
    private final Object mutex = new Object();

    @Override
    public void registerSingletonBean(String beanName, Object bean) throws SingletonBeanRegistered {
        synchronized (mutex) {
            if (injectableInstanceMap.get(beanName) != null) {
                throw new SingletonBeanRegistered(beanName + " has been registered");
            }
            injectableInstanceMap.put(beanName, bean);
            injectableBeanNameSet.add(beanName);
        }
    }

    @Override
    public void registerSingletonBean(Class<?> clazz, Object bean) throws SingletonBeanRegistered {
        String beanName = clazz.getName();
        registerSingletonBean(beanName, bean);
    }

    @Override
    public boolean containsBean(String name) {
        return injectableBeanNameSet.contains(name);
    }

    @Override
    public boolean containsBean(Class<?> clazz) {
        return injectableBeanNameSet.contains(clazz.getName());
    }

    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        return clazz.cast(getBeanByName(clazz.getName()));
    }

    public Object getBeanByName(String beanName) {
        return injectableInstanceMap.get(beanName);
    }
}
