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
     * @param clzLoaderToUse the classLoader to use
     * @return set of classes
     */
    Set<Class<?>> scanBeanClasses(ClassLoader clzLoaderToUse);
}
