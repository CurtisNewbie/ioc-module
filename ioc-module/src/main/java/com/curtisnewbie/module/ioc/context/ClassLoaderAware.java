package com.curtisnewbie.module.ioc.context;

/**
 * The bean that is aware of the ClassLoader
 *
 * @author yongjie.zhuang
 */
public interface ClassLoaderAware {

    /**
     * Notify the class loader that it should use
     */
    void setClassLoader(ClassLoader classLoader);
}
