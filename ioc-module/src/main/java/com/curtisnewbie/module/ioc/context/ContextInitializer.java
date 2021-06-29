package com.curtisnewbie.module.ioc.context;

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
}

