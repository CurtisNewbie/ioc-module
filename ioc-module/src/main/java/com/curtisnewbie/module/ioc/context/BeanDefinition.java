package com.curtisnewbie.module.ioc.context;

/**
 * Definition of bean
 *
 * @author yongjie.zhuang
 */
public interface BeanDefinition {

    /**
     * Get bean's type
     */
    Class<?> getType();

    /**
     * Get bean's name
     */
    String getName();

}
