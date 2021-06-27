package com.curtisnewbie.module.ioc.annotations;


import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Implementation of {@link AnnotatedBeanResolver}
 *
 * @author yongjie.zhuang
 */
public class AnnotatedBeanResolverImpl implements AnnotatedBeanResolver {

    @Override
    public <T extends Annotation> Set<Class<?>> resolveClazzWithAnnotation(Class<T> annotation, ClassLoader clzLoaderToUse) {
        Reflections r = new Reflections(clzLoaderToUse);
        return r.getTypesAnnotatedWith(annotation);
    }
}
