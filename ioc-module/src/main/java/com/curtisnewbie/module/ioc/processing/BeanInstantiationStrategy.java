package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.context.BeanDefinition;

/**
 * Strategy to instantiate beans (not including dependency injection)
 *
 * @author yongjie.zhuang
 */
public interface BeanInstantiationStrategy {

    /**
     * Instantiate bean
     */
    Object instantiateBean(BeanDefinition beanDefinition);

}
