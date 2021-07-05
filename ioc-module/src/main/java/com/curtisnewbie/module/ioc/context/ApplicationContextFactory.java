package com.curtisnewbie.module.ioc.context;

/**
 * Factory of context
 *
 * @author yongjie.zhuang
 */
public final class ApplicationContextFactory {

    private ApplicationContextFactory() {
    }

    /**
     * Get a new, configurable ContextInitializer
     */
    public static ConfigurableContextInitializer getNewContextInitializer() {
        return new DefaultConfigurableContextInitializer();
    }
}
