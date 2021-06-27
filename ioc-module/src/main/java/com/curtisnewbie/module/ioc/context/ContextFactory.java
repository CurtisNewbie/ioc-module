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
     * Get ContextInitializer
     */
    public static ContextInitializer getContextInitializer() {
        return new DefaultApplicationContext();
    }
}
