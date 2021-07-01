package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.context.processing.BeanPostProcessor;

/**
 * Initializer of the context
 *
 * @author yongjie.zhuang
 * @see ContextFactory
 */
public interface ContextInitializer {

    /**
     * Initialize the context
     *
     * @param mainClazz the class that contains the main(...) method
     * @return ApplicationContext
     */
    ApplicationContext initialize(Class<?> mainClazz);

    /**
     * Register a {@link BeanPostProcessor} to be used by the context
     */
    void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}

