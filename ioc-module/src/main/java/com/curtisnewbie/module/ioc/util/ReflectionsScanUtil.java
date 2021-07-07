package com.curtisnewbie.module.ioc.util;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class scanning util using {@link Reflections}
 *
 * @author yongjie.zhuang
 */
public final class ReflectionsScanUtil {

    private ReflectionsScanUtil() {
    }

    /**
     * Scan classes with the given annotation
     *
     * @param annotationClz annotation
     * @param packageUrl    package url to scan
     * @param classLoader   classloader to use
     * @param <T>           type of annotation
     */
    public static <T extends Annotation> Set<Class<?>> scanClassWithAnnotation(Class<T> annotationClz,
                                                                               String packageUrl,
                                                                               ClassLoader classLoader) {
        Reflections r = getReflectionsForAnnotationScanning(packageUrl, classLoader);
        return scanClassWithAnnotation(annotationClz, r);
    }

    /**
     * Scan classes with the given annotation
     *
     * @param annotationClz annotation
     * @param reflections   pre-instantiated reflections
     * @param <T>           type of annotation
     */
    public static <T extends Annotation> Set<Class<?>> scanClassWithAnnotation(Class<T> annotationClz,
                                                                               Reflections reflections) {
        return reflections.getTypesAnnotatedWith(annotationClz);
    }

    /**
     * Scan subclasses of the given class
     *
     * @param parentClz   parent class
     * @param packageUrl  package url to scan
     * @param classLoader classloader to use
     * @param <T>         type of parent class
     */
    public static <T> Set<Class<? extends T>> scanSubClassOf(Class<T> parentClz,
                                                             String packageUrl,
                                                             ClassLoader classLoader) {
        Reflections r = getReflectionsForSubTypeScanning(packageUrl, classLoader);
        Set<Class<? extends T>> clzSet = r.getSubTypesOf(parentClz);
        return clzSet == null ? new HashSet<>() : clzSet;
    }

    /**
     * Scan subclasses of the given class
     *
     * @param parentClz   parent class
     * @param reflections reflections to use
     * @param <T>         type of parent class
     */
    public static <T> Set<Class<? extends T>> scanSubClassOf(Class<T> parentClz,
                                                             Reflections reflections) {
        Set<Class<? extends T>> clzSet = reflections.getSubTypesOf(parentClz);
        return clzSet == null ? new HashSet<>() : clzSet;
    }

    /**
     * Scan subclasses of the given parent classes
     *
     * @param parentClz   parent class
     * @param packageUrl  package url to scan
     * @param classLoader classloader to use
     */
    public static Set<Class<?>> scanSubClassesOf(List<Class<?>> parentClz,
                                                 String packageUrl,
                                                 ClassLoader classLoader) {
        Reflections r = getReflectionsForSubTypeScanning(packageUrl, classLoader);
        Set<Class<?>> clzSet = new HashSet<>();
        for (Class fc : parentClz) {
            clzSet.addAll(scanSubClassOf(fc, r));
        }
        return clzSet;
    }

    /**
     * Get {@code Reflections} for annotation scanning, which can be reused
     *
     * @param packageUrl  package url to scan
     * @param classLoader classloader to use
     * @return reflections object
     */
    public static Reflections getReflectionsForAnnotationScanning(String packageUrl,
                                                                  ClassLoader classLoader) {
        Reflections r = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageUrl))
                .addClassLoader(classLoader)
                .addScanners(new SubTypesScanner(), new TypeAnnotationsScanner()));
        return r;
    }

    /**
     * Get {@code Reflections} for subtype scanning, which can be reused
     *
     * @param packageUrl  package url to scan
     * @param classLoader classloader to use
     * @return reflections object
     */
    public static Reflections getReflectionsForSubTypeScanning(String packageUrl,
                                                               ClassLoader classLoader) {
        Reflections r = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageUrl))
                .addClassLoader(classLoader)
                .addScanners(new SubTypesScanner()));
        return r;
    }

    /**
     * Get {@code Reflections} for non-class resource scanning, which can be reused
     *
     * @param packageUrl  package url to scan
     * @param classLoader classloader to use
     * @return reflections object
     */
    public static Reflections getReflectionsForResourcesScanning(String packageUrl,
                                                                 ClassLoader classLoader) {
        Reflections r = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageUrl))
                .addClassLoader(classLoader)
                .addScanners(new ResourcesScanner()));
        return r;
    }
}
