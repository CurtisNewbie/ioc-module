package com.curtisnewbie.module.ioc.context;

import java.beans.PropertyDescriptor;

/**
 * Info of property
 *
 * @author yongjie.zhuang
 */
public class PropertyInfo {

    private Class<?> propertyBeanType;

    /**
     * Name of the field/property
     */
    private String propertyName;

    /**
     * Descriptor of the property
     */
    private PropertyDescriptor propertyDescriptor;

    public PropertyInfo(String propertyName, PropertyDescriptor propertyDescriptor, Class<?> propertyBeanType) {
        this.propertyName = propertyName;
        this.propertyDescriptor = propertyDescriptor;
        this.propertyBeanType = propertyBeanType;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Class<?> getPropertyBeanType() {
        return propertyBeanType;
    }

    public void setPropertyBeanType(Class<?> propertyBeanType) {
        this.propertyBeanType = propertyBeanType;
    }
}
