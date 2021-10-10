package com.curtisnewbie.module.ioc.processing;

/**
 * Post processor of bean
 *
 * @author yongjie.zhuang
 */
public interface BeanPostProcessor {

    /**
     * Post-processing bean before initialization
     *
     * @param beanName bean's name
     * @param bean     bean
     * @return bean to use
     */
    default Object postProcessBeforeInitialization(String beanName, Object bean) {
        return bean;
    }

    /**
     * Post-processing bean after initialization
     *
     * @param beanName bean's name
     * @param bean     bean
     * @return bean to use
     */
    default Object postProcessAfterInitialization(String beanName, Object bean) {
        return bean;
    }

}
