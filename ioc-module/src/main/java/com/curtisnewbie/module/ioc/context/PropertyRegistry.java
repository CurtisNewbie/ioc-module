package com.curtisnewbie.module.ioc.context;

/**
 * Registry of property values
 *
 * @author yongjie.zhuang
 */
public interface PropertyRegistry {

    /**
     * Get property
     *
     * @param key key
     * @return value
     */
    Object getProperty(String key);

    /**
     * Set property, if the key exists already, if may simply replace the value
     *
     * @param key   key
     * @param value value
     */
    void setProperty(String key, Object value);

    /**
     * Check if the registry contains the property
     *
     * @param key key
     */
    boolean containsProperty(String key);

}
