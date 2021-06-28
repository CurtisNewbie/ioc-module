package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.exceptions.SingletonBeanRegistered;

/**
 * Registry of singleton beans
 *
 * @author yongjie.zhuang
 */
public interface SingletonBeanRegistry extends BeanRegistry {

    /**
     * Register a singleton bean with the name
     *
     * @param beanName name of the bean
     * @param bean     the instance/bean
     */
    void registerSingletonBean(String beanName, Object bean) throws SingletonBeanRegistered;

    /**
     * Register a singleton bean with the class
     *
     * @param clazz class of the bean
     * @param bean  the instance/bean
     */
    void registerSingletonBean(Class<?> clazz, Object bean) throws SingletonBeanRegistered;

    /**
     * Register a dependency of this bean
     */
    void registerDependency(String beanName, String dependentBeanName);

    /**
     * Check whether the given bean is dependent on the other bean (either directly or indirectly)
     *
     * @param beanName          given bean
     * @param dependentBeanName the bean that might be dependent on
     */
    boolean isDependent(String beanName, String dependentBeanName);
}
