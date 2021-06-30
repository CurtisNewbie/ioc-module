package com.curtisnewbie.module.ioc.context;


import com.curtisnewbie.module.ioc.util.ClassLoaderHolder;
import com.curtisnewbie.module.ioc.util.ReflectionsScanUtil;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Abstract implementation of {@link BeanClassScanner}
 *
 * @author yongjie.zhuang
 */
public abstract class AbstractBeanClassScanner implements BeanClassScanner {

    protected ClassLoader classLoader;

    protected static final String ROOT_PATH = "";

    protected <T extends Annotation> Set<Class<?>> scanClassWithAnnotation(Class<T> annotationClz) {
        return ReflectionsScanUtil.scanClassWithAnnotation(annotationClz,
                ROOT_PATH,
                getIfNotNullElseDefaultClassLoader());
    }

    protected Set<Class<?>> scanSubClassOf(List<Class<?>> parentClz) {
        return ReflectionsScanUtil.scanSubClassesOf(parentClz,
                ROOT_PATH,
                getIfNotNullElseDefaultClassLoader());
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        Objects.requireNonNull(classLoader);
        this.classLoader = classLoader;
    }

    /**
     * Get classloader used for this scanner, if {@code this.classLoader == null}, then return a default class loader to
     * use
     */
    protected ClassLoader getIfNotNullElseDefaultClassLoader() {
        if (this.classLoader == null) {
            this.classLoader = ClassLoaderHolder.getClassLoader();
        }
        return this.classLoader;
    }

}
