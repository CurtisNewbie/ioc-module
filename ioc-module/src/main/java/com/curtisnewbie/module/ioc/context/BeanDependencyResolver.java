package com.curtisnewbie.module.ioc.context;

import java.util.List;
import java.util.Map;

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
     * @return Map of accessible and injectable dependencies of the given class, where the key is the dependent bean
     * name, and value is list of PropertyInfo which is of the same dependent type
     */
    Map<String, List<PropertyInfo>> resolveDependenciesOfClass(Class<?> clazz);
}
