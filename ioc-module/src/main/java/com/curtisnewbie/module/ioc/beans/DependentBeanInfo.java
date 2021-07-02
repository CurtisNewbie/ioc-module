package com.curtisnewbie.module.ioc.beans;

import java.util.List;

/**
 * Information of a dependent Bean and where (which properties/fields) it will be injected into
 *
 * @author yongjie.zhuang
 */
public interface DependentBeanInfo {

    /**
     * Get the dependent bean's name
     */
    String getDependentBeanName();

    /**
     * Get list of properties that will inject this dependent bean
     */
    List<BeanPropertyInfo> getBeanPropertiesToInject();
}
