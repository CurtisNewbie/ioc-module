package com.curtisnewbie.module.ioc.context;

/**
 * Post processor of bean
 *
 * @author yongjie.zhuang
 */
public interface BeanPostProcessor {

    /**
     * Do post processing of bean
     *
     * @param bean     bean's instance
     * @param beanName bean's name
     */
    void postProcessBean(Object bean, String beanName);

}
