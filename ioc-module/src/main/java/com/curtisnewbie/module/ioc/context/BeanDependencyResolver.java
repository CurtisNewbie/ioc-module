package com.curtisnewbie.module.ioc.context;

import java.util.Set;

/**
 * Resolver of beans' dependencies
 *
 * @author yongjie.zhuang
 */
public interface BeanDependencyResolver {

    /**
     * Resolve dependencies of the class
     *
     * @param clazz class
     * @return Set of accessible and injectable dependencies of the given class
     */
    Set<Class<?>> resolveDependenciesOfClass(Class<?> clazz);

    /**
     * Resolve dependencies of the class
     *
     * @param clazz class
     * @return Set of accessible and injectable dependencies of the given class
     */
    Set<String> resolveDependenciesNamesOfClass(Class<?> clazz);
}
