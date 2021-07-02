package com.curtisnewbie.module.ioc.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Utility class for beans processing
 *
 * @author yongjie.zhuang
 */
public final class BeansUtil {

    private BeansUtil() {
    }

    /**
     * Introspect the bean to get it's {@link BeanInfo}
     *
     * @param clz class
     * @return BeanInfo of this bean
     */
    public static BeanInfo introspectBeanInfo(Class<?> clz) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clz);
            return beanInfo;
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Unable to introspect " + clz, e);
        }
    }

    /**
     * Introspect the bean to get it's {@link PropertyDescriptor}s
     *
     * @param clz class
     * @return array of PropertyDescriptor of this bean
     */
    public static PropertyDescriptor[] introspectPropertyDescriptors(Class<?> clz) {
        return introspectBeanInfo(clz).getPropertyDescriptors();
    }

}
