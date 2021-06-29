package com.curtisnewbie.module.ioc.context;

/**
 * Factory of context
 *
 * @author yongjie.zhuang
 */
public class ContextFactory {

    private ContextFactory() {
    }

    /**
     * Get a new ContextInitializer (only one should be used)
     */
    public static ContextInitializer getContextInitializer() {
        return new DefaultApplicationContext();
    }
}
