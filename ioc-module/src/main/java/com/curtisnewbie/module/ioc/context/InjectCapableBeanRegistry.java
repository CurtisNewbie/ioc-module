package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.processing.BeanPostProcessor;

/**
 * @author yongjie.zhuang
 */
public interface InjectCapableBeanRegistry extends SingletonBeanRegistry, BeanAliasRegistry, BeanDefinitionRegistry {

    /**
     * Register a dependency of this bean
     */
    void registerDependency(String beanName, String dependentBeanName);

    /**
     * Check whether the given bean is dependent on the other bean (either directly or indirectly)
     *
     * @param beanName          given bean
     * @param dependentBeanName the bean that might be dependent on
     */
    boolean isDependent(String beanName, String dependentBeanName);

    /**
     * Register bean post processor
     */
    void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
