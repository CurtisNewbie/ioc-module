package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.context.processing.AbstractBeanClassScanner;
import com.curtisnewbie.module.ioc.context.processing.BeanClassScanner;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yongjie.zhuang
 */
public class MockBeanClassScanner extends AbstractBeanClassScanner implements BeanClassScanner {

    private Set<Class<?>> beanClassesFound = new HashSet<>();

    @Override
    public Set<Class<?>> scanBeanClasses() {
        return beanClassesFound;
    }

    public void setBeanClassesFound(Set<Class<?>> mockClasses) {
        this.beanClassesFound = mockClasses;
    }

}
