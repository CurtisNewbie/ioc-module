package com.curtisnewbie.module.ioc.context;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Info of property
 *
 * @author yongjie.zhuang
 */
public class PropertyInfo {

    /**
     * Name of the field/property
     */
    private final String propertyName;

    /**
     * Descriptor of the property
     */
    private final PropertyDescriptor propertyDescriptor;

    public PropertyInfo(String propertyName, PropertyDescriptor propertyDescriptor) {
        Objects.requireNonNull(propertyName);
        Objects.requireNonNull(propertyDescriptor);

        this.propertyName = propertyName;
        this.propertyDescriptor = propertyDescriptor;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Check whether this property's type is assignable from the given type
     */
    public boolean isPropertyTypeAssignableFrom(Class<?> type) {
        return this.getPropertyDescriptor().getPropertyType().isAssignableFrom(type);
    }

    /**
     * Set property value to the {@code targetObject} using write method
     *
     * @param targetObject target object
     * @param value        value to be set
     * @throws ReflectiveOperationException when the operation is failed
     */
    public void writeValueToPropertyOfBean(Object targetObject, Object value) throws ReflectiveOperationException {
        Method writeMethod = propertyDescriptor.getWriteMethod();
        writeMethod.setAccessible(true);
        writeMethod.invoke(targetObject, value);
    }
}
