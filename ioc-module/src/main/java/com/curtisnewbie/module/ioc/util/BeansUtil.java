package com.curtisnewbie.module.ioc.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        Objects.requireNonNull(clz);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clz);
            return beanInfo;
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Unable to introspect " + clz, e);
        }
    }

    /**
     * Introspect the bean to get all its declared fields
     *
     * @param clz class
     * @see #introspectBeanFieldsMap(Class)
     */
    public static Field[] introspectBeanFields(Class<?> clz) {
        Objects.requireNonNull(clz);
        return clz.getDeclaredFields();
    }

    /**
     * Introspect the bean to get all its declared fields in forms of a Map (field name to Field)
     *
     * @param clz class
     * @return map: field name to Field object
     */
    public static Map<String, Field> introspectBeanFieldsMap(Class<?> clz) {
        Field[] fields = introspectBeanFields(clz);
        Map<String, Field> nameFieldMap = new HashMap<>();
        for (Field f : fields) {
            nameFieldMap.put(f.getName(), f);
        }
        return nameFieldMap;
    }

    /**
     * Introspect the bean to get it's {@link PropertyDescriptor}s
     *
     * @param clz class
     * @return array of PropertyDescriptor of this bean
     */
    public static PropertyDescriptor[] introspectPropertyDescriptors(Class<?> clz) {
        Objects.requireNonNull(clz);
        return introspectBeanInfo(clz).getPropertyDescriptors();
    }

    /**
     * Introspect the bean to get it's {@link PropertyDescriptor}s in forms of a map (property name to
     * PropertyDescriptor)
     *
     * @param clz class
     * @return map: property name to PropertyDescriptor
     */
    public static Map<String, PropertyDescriptor> introspectPropertyDescriptorMap(Class<?> clz) {
        PropertyDescriptor[] props = introspectPropertyDescriptors(clz);
        Map<String, PropertyDescriptor> propMap = new HashMap<>();
        for (PropertyDescriptor p : props) {
            propMap.put(p.getName(), p);
        }
        return propMap;
    }
}
