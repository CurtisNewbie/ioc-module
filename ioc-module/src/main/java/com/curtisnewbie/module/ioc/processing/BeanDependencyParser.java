package com.curtisnewbie.module.ioc.processing;


import com.curtisnewbie.module.ioc.beans.BeanPropertyInfo;

import java.util.List;
import java.util.Map;

/**
 * Parser of beans' dependencies
 *
 * @author yongjie.zhuang
 */
public interface BeanDependencyParser {

    /**
     * Parse dependencies of the class
     *
     * @param clazz class
     * @return Map of accessible and injectable dependencies of the given class; where the key is the dependent bean's
     * name, and value is a list of {@link BeanPropertyInfo} of this type that needs to be resolved
     */
    Map<String, List<BeanPropertyInfo>> parseDependenciesOfClass(Class<?> clazz);
}
