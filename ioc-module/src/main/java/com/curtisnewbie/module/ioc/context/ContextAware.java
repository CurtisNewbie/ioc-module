package com.curtisnewbie.module.ioc.context;

/**
 * Bean that is aware of the context
 *
 * @author yongjie.zhuang
 */
public interface ContextAware {

    /**
     * Get bean by Class
     *
     * @param clazz class of the bean
     * @param <T>   type of the bean
     * @return bean
     */
    <T> T getBeanByClass(Class<T> clazz);

    /**
     * Get bean by name
     *
     * @param beanName name of the bean
     * @return bean
     */
    Object getBeanByName(String beanName);

}
