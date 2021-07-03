package com.curtisnewbie.module.ioc.context;

import java.util.Objects;

/**
 * Implementation of {@link BeanNameGenerator} that uses fully qualified name ({@link Class#getCanonicalName()})
 *
 * @author yongjie.zhuang
 */
public class BeanQualifiedNameGenerator implements BeanNameGenerator {

    @Override
    public String generateBeanName(Class<?> beanClazz) {
        Objects.requireNonNull(beanClazz);
        return beanClazz.getCanonicalName();
    }
}
