package com.curtisnewbie.module.ioc.context;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yongjie.zhuang
 */
public class MockBeanClassScanner implements BeanClassScanner {

    private Set<Class<?>> beanClassesFound = new HashSet<>();

    @Override
    public Set<Class<?>> scanBeanClasses(ClassLoader clzLoaderToUse) {
        return beanClassesFound;
    }

    public void setBeanClassesFound(Set<Class<?>> mockClasses) {
        this.beanClassesFound = mockClasses;
    }

}
