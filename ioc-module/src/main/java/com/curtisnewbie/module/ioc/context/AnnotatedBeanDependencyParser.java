package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.exceptions.TypeNotSupportedForInjectionException;
import com.curtisnewbie.module.ioc.exceptions.UnableToInjectDependencyException;
import com.curtisnewbie.module.ioc.util.BeanNameUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Parser of annotated beans' dependencies
 *
 * @author yongjie.zhuang
 */
public class AnnotatedBeanDependencyParser implements BeanDependencyParser {

    @Override
    public Map<String, List<PropertyInfo>> parseDependenciesOfClass(Class<?> clz) {
        Objects.requireNonNull(clz, "class is null, unable to parse dependencies");
        Map<String, List<PropertyInfo>> dependencies = new HashMap<>();
        Map<String, PropertyDescriptor> pdMap;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            pdMap = propertyDescriptorsToMap(propertyDescriptors);
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Unable to introspect " + clz, e);
        }

        Field[] fields = clz.getDeclaredFields();
        for (Field f : fields) {
            // the field has a @Dependency annotation & it contains a writer method
            if (f.isAnnotationPresent(Dependency.class)) {
                PropertyDescriptor pd = pdMap.get(f.getName());
                // if pd == null, means there is not getter and setter for this field at all
                // as long as the writerMethod is missing, this field is not injectable
                if (pd == null || pd.getWriteMethod() == null) {
                    throw new UnableToInjectDependencyException(
                            String.format("Unable to inject dependency, field: '%s' not accessible, setter method is missing",
                                    f.getName())
                    );
                }
                Class<?> propType = pd.getPropertyType();
                if (isBoxedPrimitiveType(propType)) {
                    throw new TypeNotSupportedForInjectionException(
                            "Basic/Primitive types are not supported for dependency injection, " +
                                    "field: " + f.getName());
                }
                if (isCollections(propType)) {
                    throw new TypeNotSupportedForInjectionException(
                            "Collections are not supported for dependency injection, " +
                                    "field: " + f.getName());
                }
                String dependentBeanName = BeanNameUtil.toBeanName(propType);
                // key: dependent's type, value: list of propertyInfo of this dependency type
                dependencies.computeIfAbsent(dependentBeanName, k -> new ArrayList<>());
                dependencies.get(dependentBeanName).add(new PropertyInfo(f.getName(), pd));
            }
        }
        return dependencies;
    }

    /**
     * Check if the class is boxed primitive type, e.g., Integer, Double, etc.
     *
     * @param clz class
     */
    private boolean isBoxedPrimitiveType(Class<?> clz) {
        if (clz.isPrimitive()) {
            return true;
        }
        List<Class<?>> boxedTypes = Arrays.asList(
                Integer.class,
                String.class,
                Double.class,
                Float.class,
                Long.class,
                Short.class,
                Byte.class
        );
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
    private Map<String, PropertyDescriptor> propertyDescriptorsToMap(PropertyDescriptor[] pds) {
        Map<String, PropertyDescriptor> pdMap = new HashMap<>();
        for (PropertyDescriptor pd : pds) {
            pdMap.put(pd.getName(), pd);
        }
        return pdMap;
    }
}
