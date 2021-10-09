package com.curtisnewbie.module.ioc.context;

import java.util.Set;

/**
 * Registry of {@link BeanDefinition}
 *
 * @author yongjie.zhuang
 */
public interface BeanDefinitionRegistry {

    /**
     * Get {@link BeanDefinition}
     *
     * @param beanName bean's name
     * @return beanDefinition (nullable)
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * Register beanDefinition
     *
     * @param beanName       bean's name
     * @param beanDefinition beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * Check if registry contains BeanDefinition of given name
     *
     * @param beanName bean's name
     * @return true if found else false
     */
    boolean contains(String beanName);

}
