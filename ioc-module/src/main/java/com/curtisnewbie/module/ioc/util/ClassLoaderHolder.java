package com.curtisnewbie.module.ioc.util;

/**
 * Holder of classLoader
 *
 * @author yongjie.zhuang
 */
public final class ClassLoaderHolder {

    private ClassLoaderHolder() {
    }

    /**
     * Get default class loader
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoaderHolder.class.getClassLoader();
        }
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        return cl;
    }

}
