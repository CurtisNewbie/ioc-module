package com.curtisnewbie.module.ioc.annotations;

import java.util.Set;

/**
 * Resolver of annotated beans
 *
 * @author yongjie.zhuang
 * @see Dependency
 * @see MBean
 */
public interface AnnotatedBeanResolver {

    /**
     * Resolve beans' classes
     *
     * @param clzLoaderToUse the classLoader to use
     * @return set of classes
     */
    Set<Class<?>> resolveBeanClasses(ClassLoader clzLoaderToUse);

    /**
     * Resolve dependencies of the class
     *
     * @param clazz class
     * @return Set of accessible and injectable dependencies of the given class
     */
    Set<Class<?>> resolveDependenciesOfClass(Class<?> clazz);
}
