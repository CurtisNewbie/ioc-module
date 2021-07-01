package com.curtisnewbie.module.ioc.context.processing;

/**
 * Post processor of bean
 *
 * @author yongjie.zhuang
 */
public interface BeanPostProcessor {

    /**
     * Do post processing of bean (after bean instantiation and dependency injection)
     *
     * @param bean     bean's instance
     * @param beanName bean's name
     * @return bean after processing (e.g., if the post-processing is for adding a proxy, then the returned object will
     * be the proxy)
     */
    Object postProcessBean(Object bean, String beanName);

}
