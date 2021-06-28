package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.util.BeanNameUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolver of annotated beans' dependencies
 *
 * @author yongjie.zhuang
 */
public class AnnotatedBeanDependencyResolver implements BeanDependencyResolver {

    @Override
    public Set<Class<?>> resolveDependenciesOfClass(Class<?> clz) {
        Objects.requireNonNull(clz, "class is null, unable to resolve dependencies");
        Set<Class<?>> dependentClasses = new HashSet<>();
        Map<String, PropertyDescriptor> pdMap;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            pdMap = propertyDescriptorToMap(propertyDescriptors);
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Unable to introspect " + clz, e);
        }

        Field[] fields = clz.getDeclaredFields();
        for (Field f : fields) {
            // the field has a @Dependency annotation & it contains a writer method
            if (f.isAnnotationPresent(Dependency.class)) {
                PropertyDescriptor pd = pdMap.get(f.getName());
                Objects.requireNonNull(pd, f.getName());
                if (pd.getWriteMethod() != null) {
                    Class<?> propType = pd.getPropertyType();
                    if (isBoxedPrimitiveType(propType)) {
                        throw new IllegalStateException("Basic/Primitive types are not supported for dependency injection, " +
                                "field: " + f.getName());
                    }
                    if (isCollections(propType)) {
                        throw new IllegalStateException("Collections are not supported for dependency injection, " +
                                "field: " + f.getName());
                    }
                    dependentClasses.add(propType);
                }
            }
        }
        return dependentClasses;
    }

    @Override
    public Set<String> resolveDependenciesNamesOfClass(Class<?> clazz) {
        return resolveDependenciesOfClass(clazz)
                .stream()
                .map(c -> BeanNameUtil.toBeanName(c))
                .collect(Collectors.toSet());
    }

    /**
     * Check if the class is boxed primitive type, e.g., Integer, Double, etc.
     *
     * @param clz class
     */
    private boolean isBoxedPrimitiveType(Class<?> clz) {
        List<Class<?>> boxedTypes = Arrays.asList(Integer.class, String.class, Double.class, Float.class, Long.class, Short.class);
        return isTypes(clz, boxedTypes);
    }

    /**
     * Check if the class is a Collection
     *
     * @param clz class
     */
    private boolean isCollections(Class<?> clz) {
        List<Class<?>> boxedTypes = Arrays.asList(Collection.class);
        return isTypes(clz, boxedTypes);
    }

    private boolean isTypes(Class<?> compared, List<Class<?>> toBeMatched) {
        if (toBeMatched.isEmpty()) {
            throw new IllegalArgumentException("These must be at least one type to be compared");
        }
        for (Class<?> target : toBeMatched) {
            if (compared.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Load array of PropertyDescriptor to map, where the key is the property name, and the value is the
     * PropertyDescriptor
     */
    private Map<String, PropertyDescriptor> propertyDescriptorToMap(PropertyDescriptor[] pds) {
        Map<String, PropertyDescriptor> pdMap = new HashMap<>();
        for (PropertyDescriptor pd : pds) {
            pdMap.put(pd.getName(), pd);
        }
        return pdMap;
    }
}
