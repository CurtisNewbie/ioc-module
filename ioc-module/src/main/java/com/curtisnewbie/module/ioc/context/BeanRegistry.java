package com.curtisnewbie.module.ioc.context;

/**
 * Registry of beans
 *
 * @author yongjie.zhuang
 */
public interface BeanRegistry extends ContextAware {

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
