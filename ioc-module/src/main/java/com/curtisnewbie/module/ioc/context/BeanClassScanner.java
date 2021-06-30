package com.curtisnewbie.module.ioc.context;

import java.util.Set;

/**
 * Scanner of beans' class
 *
 * @author yongjie.zhuang
 */
public interface BeanClassScanner {

    /**
     * Scan beans' class
     *
     * @return set of classes
     */
    Set<Class<?>> scanBeanClasses();

    /**
     * Set the classloader to be used by the scanner, if not being set, the default classloader will be used instead.
     *
     * @param classLoader classLoader
     */
    void setClassLoader(ClassLoader classLoader);

}
