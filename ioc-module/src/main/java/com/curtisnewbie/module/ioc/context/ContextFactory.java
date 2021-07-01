package com.curtisnewbie.module.ioc.context;

/**
 * Factory of context
 *
 * @author yongjie.zhuang
 */
public final class ContextFactory {

    private ContextFactory() {
    }

    /**
     * Get a new ContextInitializer (only one should be used)
     */
    public static ContextInitializer getNewContextInitializer() {
        return new DefaultApplicationContext();
    }
}
