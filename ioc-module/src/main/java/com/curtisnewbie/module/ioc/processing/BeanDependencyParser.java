package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.beans.DependentBeanInfo;

import java.util.List;

/**
 * Parser of beans' dependencies
 *
 * @author yongjie.zhuang
 */
public interface BeanDependencyParser {

    /**
     * Parse dependencies of the class
     *
     * @param beanClazz bean's class
     * @return dependent beans' info
     */
    List<DependentBeanInfo> parseDependencies(Class<?> beanClazz);
}
