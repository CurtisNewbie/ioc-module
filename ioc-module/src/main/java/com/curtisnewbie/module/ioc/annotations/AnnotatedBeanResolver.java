package com.curtisnewbie.module.ioc.annotations;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Resolver of annotated beans
 *
 * @author yongjie.zhuang
 * @see Dependency
 * @see Injectable
 */
public interface AnnotatedBeanResolver {

    /**
     * Resolve classes with the annotation
     *
     * @param annotation     annotation
     * @param clzLoaderToUse the classLoader to use
     * @param <T>            type of annotation
     * @return set of classes
     */
    <T extends Annotation> Set<Class<?>> resolveClazzWithAnnotation(Class<T> annotation, ClassLoader clzLoaderToUse);
}
