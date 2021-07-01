package com.curtisnewbie.module.ioc.context.processing;


import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

import java.util.*;

/**
 * Implementation of {@link BeanClassScanner}, it scans beans with specified Annotation, e.g., {@link MBean}
 *
 * @author yongjie.zhuang
 * @see Dependency
 * @see MBean
 */
public class AnnotatedBeanClassScanner extends AbstractBeanClassScanner implements BeanClassScanner {

    @Override
    public Set<Class<?>> scanBeanClasses() {
        return scanClassWithAnnotation(MBean.class);
    }
}
