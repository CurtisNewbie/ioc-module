package com.curtisnewbie.module.ioc.context;

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
    void registerSingletonBean(String beanName, Object bean);

    /**
     * Register a singleton bean with the class
     *
     * @param clazz class of the bean
     * @param bean  the instance/bean
     */
    void registerSingletonBean(Class<?> clazz, Object bean);

}
