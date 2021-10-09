package com.curtisnewbie.module.ioc.context;

import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
public class DefaultBeanDefinition implements BeanDefinition {

    private final Class<?> clazz;
    private final String beanName;

    public DefaultBeanDefinition(Class<?> clz, String beanName) {
        Objects.requireNonNull(clz);
        Objects.requireNonNull(beanName);
        this.clazz = clz;
        this.beanName = beanName;
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public String getName() {
        return beanName;
    }
}
