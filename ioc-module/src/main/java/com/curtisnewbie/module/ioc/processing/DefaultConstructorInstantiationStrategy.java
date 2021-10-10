package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.context.BeanDefinition;
import com.curtisnewbie.module.ioc.exceptions.BeanCreationException;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * Implementation of {@link BeanInstantiationStrategy}
 * <br>
 * It uses bean's default constructor to instantiate bean
 *
 * @author yongjie.zhuang
 */
public class DefaultConstructorInstantiationStrategy implements BeanInstantiationStrategy {

    @Override
    public Object instantiateBean(BeanDefinition beanDefinition) {
        Objects.requireNonNull(beanDefinition);
        Class<?> beanClazz = beanDefinition.getType();
        Objects.requireNonNull(beanClazz);
        // create bean with default constructor
        try {
            Constructor<?> defConstructor = beanClazz.getDeclaredConstructor();
            Objects.requireNonNull(defConstructor, getFailureMsg(beanClazz));
            return defConstructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new BeanCreationException(getFailureMsg(beanClazz), e);
        }
    }

    private static String getFailureMsg(Class<?> clz) {
        return "Unable to create bean: " + clz.toString() + " using default constructor";
    }
}
