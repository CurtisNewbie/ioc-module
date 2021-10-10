package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.context.BeanDefinition;

/**
 * Post processing bean before and after instantiation
 *
 * @author yongjie.zhuang
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * Do post processing for the given bean after bean instantiation
     *
     * @param beanDefinition Bean's definition
     * @return bean to use (nullable)
     */
    default Object postProcessBeanBeforeInstantiation(BeanDefinition beanDefinition) {
        return null;
    }

    /**
     * Do post processing for the given bean after bean instantiation
     *
     * @param bean     bean's instance
     * @param beanName bean's name
     * @return whether the bean still need to initialize
     */
    default boolean postProcessBeanAfterInstantiation(Object bean, String beanName) {
        return true;
    }

}
