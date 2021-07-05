package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.config.LogMutable;

/**
 * Initializer of the context
 *
 * @author yongjie.zhuang
 * @see ApplicationContextFactory
 */
public interface ContextInitializer extends LogMutable {

    /**
     * Initialize the context
     *
     * @param mainClazz the class that contains the main(...) method
     * @return ApplicationContext
     */
    ApplicationContext initialize(Class<?> mainClazz);

}

