package com.curtisnewbie.module.ioc.processing;

/**
 * Generator of bean's name
 *
 * @author yongjie.zhuang
 */
public interface BeanNameGenerator {

    /**
     * Generate bean name by class
     */
    String generateBeanName(Class<?> beanClazz);
}
