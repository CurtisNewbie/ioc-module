package com.curtisnewbie.module.ioc.context;

/**
 * Factory of context
 *
 * @author yongjie.zhuang
 */
public class ContextFactory {

    private static final ContextInitializer contextInitializer = new DefaultApplicationContext();

    private ContextFactory() {
    }

    /**
     * Get cached ContextInitializer
     */
    public static ContextInitializer getContextInitializer() {
        return contextInitializer;
    }
}
