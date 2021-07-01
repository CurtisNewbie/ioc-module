package com.curtisnewbie.module.ioc.context.processing;

/**
 * Strategy to instantiate beans (not including dependency injection)
 *
 * @author yongjie.zhuang
 */
public interface BeanInstantiationStrategy {

    /**
     * Instantiate bean by the given bean class
     */
    Object instantiateBean(Class<?> beanClazz);

}
