package com.curtisnewbie.module.ioc.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link PropertyRegistry}
 *
 * @author yongjie.zhuang
 * @see com.curtisnewbie.module.ioc.processing.PropertyValueBeanPostProcessor
 */
public class DefaultPropertyRegistry implements PropertyRegistry {

    private final Map<String, Object> propertyValues = new ConcurrentHashMap<>();

    @Override
    public Object getProperty(String key) {
        return propertyValues.get(propertyValues);
    }

    @Override
    public void setProperty(String key, Object value) {
        propertyValues.put(key, value);
    }

    @Override
    public boolean containsProperty(String key) {
        return propertyValues.containsKey(key);
    }
}
