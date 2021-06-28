package com.curtisnewbie.module.ioc.context;

import java.beans.Introspector;

/**
 * Registry of beans
 *
 * @author yongjie.zhuang
 */
public interface BeanRegistry {

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

    /**
     * Check if the context contains bean of the given name
     *
     * @param name name of the bean
     */
    boolean containsBean(String name);

    /**
     * Check if the context contains bean of the given class
     *
     * @param clazz class of th bean
     */
    boolean containsBean(Class<?> clazz);
}
