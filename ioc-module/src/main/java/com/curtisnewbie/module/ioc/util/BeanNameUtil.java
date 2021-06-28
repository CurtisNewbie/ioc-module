package com.curtisnewbie.module.ioc.util;

import java.beans.Introspector;
import java.util.Objects;

/**
 * Util for bean name processing
 *
 * @author yongjie.zhuang
 */
public final class BeanNameUtil {

    private BeanNameUtil() {
    }

    /**
     * Unified way to convert Class to a bean's name
     *
     * @param clazz class
     * @return bean name
     */
    public static String toBeanName(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getCanonicalName();
    }
}
