package com.curtisnewbie.module.ioc.beans;

import com.curtisnewbie.module.ioc.exceptions.UnableToInjectDependencyException;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Default implementation of {@link com.curtisnewbie.module.ioc.beans.BeanPropertyInfo} using PropertyDescriptor and
 * writeMethod
 * <p>
 * It uses property's type (via {@link Class#isAssignableFrom(Class)}) to determine whether the given type can satisfy
 * the type required by the property, and it also uses {@code writeMethod} to actually inject property value.
 * </p>
 *
 * @author yongjie.zhuang
 */
public class DefaultBeanPropertyInfo implements BeanPropertyInfo {

    /**
     * Name of the field/property
     */
    private final String propertyName;

    /**
     * Descriptor of the property
     */
    private final PropertyDescriptor propertyDescriptor;

    public DefaultBeanPropertyInfo(String propertyName, PropertyDescriptor propertyDescriptor) {
        Objects.requireNonNull(propertyName);
        Objects.requireNonNull(propertyDescriptor);

        this.propertyName = propertyName;
        this.propertyDescriptor = propertyDescriptor;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public boolean canSatisfyRequiredType(Class<?> type) {
        return this.propertyDescriptor.getPropertyType().isAssignableFrom(type);
    }

    /**
     * Set property value to the {@code targetObject} using write method
     *
     * @param targetObject target object
     * @param value        value to be set
     * @throws UnableToInjectDependencyException when the operation is failed
     */
    @Override
    public void setValueToPropertyOfBean(Object targetObject, Object value) {
        try {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            writeMethod.setAccessible(true);
            writeMethod.invoke(targetObject, value);
        } catch (ReflectiveOperationException e) {
            throw new UnableToInjectDependencyException("Unable to inject dependency in field: " + getPropertyName());
        }
    }
}
