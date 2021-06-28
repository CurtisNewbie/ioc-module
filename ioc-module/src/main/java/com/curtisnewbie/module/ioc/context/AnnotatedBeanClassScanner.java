package com.curtisnewbie.module.ioc.context;


import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.*;

/**
 * Implementation of {@link BeanClassScanner}, it scans beans with specified Annotation, e.g., {@link MBean}
 *
 * @author yongjie.zhuang
 * @see Dependency
 * @see MBean
 */
public class AnnotatedBeanClassScanner implements BeanClassScanner {

    @Override
    public Set<Class<?>> scanBeanClasses(ClassLoader clzLoaderToUse) {
        Reflections r = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(""))
                .addClassLoader(clzLoaderToUse)
                .addScanners(new SubTypesScanner(), new TypeAnnotationsScanner()));
        return r.getTypesAnnotatedWith(MBean.class);
    }

}
