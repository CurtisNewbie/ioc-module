package com.curtisnewbie.module.ioc.beans;

import java.util.List;

/**
 * Default implementation of {@link DependentBeanInfo}
 *
 * @author yongjie.zhuang
 */
public class DefaultDependentBeanInfo implements DependentBeanInfo {

    /**
     * This dependent bean's name
     */
    private final String beanName;

    /**
     * List of propertyInfo that this dependent bean will be injected into
     */
    private final List<BeanPropertyInfo> propertiesToInject;

    public DefaultDependentBeanInfo(String beanName, List<BeanPropertyInfo> propertiesToInject) {
        this.beanName = beanName;
        this.propertiesToInject = propertiesToInject;

    }

    @Override
    public String getDependentBeanName() {
        return beanName;
    }

    @Override
    public List<BeanPropertyInfo> getBeanPropertiesToInject() {
        return propertiesToInject;
    }
}
