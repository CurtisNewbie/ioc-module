package com.curtisnewbie.module.ioc.beans;

/**
 * Representation of a type's property
 * <p>
 * It's not actually bound to a specific bean. It describes the info of a specific property of a bean's type, and
 * provides methods to actually set the value to the property of a target object.
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface BeanPropertyInfo {

    /**
     * Get name of the property/field
     */
    String getPropertyName();

    /**
     * Check if the given type can satisfy the type required by this property
     *
     * @param type type
     */
    boolean canSatisfyRequiredType(Class<?> type);

    /**
     * Set property value to the {@code targetObject}
     *
     * @param targetObject target object
     * @param value        value to be set
     */
    void setValueToPropertyOfBean(Object targetObject, Object value);
}
