package com.curtisnewbie.module.ioc.processing;

/**
 * Post processor of bean
 *
 * @author yongjie.zhuang
 */
public interface BeanPostProcessor {

    /**
     * Do post processing for the given bean after bean instantiation
     *
     * @param bean     bean's instance
     * @param beanName bean's name
     * @return bean after processing (e.g., if the post-processing is for adding a proxy, then the returned object will
     * be the proxy)
     */
    Object postProcessBeanAfterInstantiation(Object bean, String beanName);

}
