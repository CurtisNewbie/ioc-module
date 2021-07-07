package com.curtisnewbie.module.ioc.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract implementation of {@link PropertyRegistry} that provides basic functionalities for adding and getting
 * properties
 *
 * @author yongjie.zhuang
 * @see com.curtisnewbie.module.ioc.processing.PropertyValueBeanPostProcessor
 * @see com.curtisnewbie.module.ioc.convert.StringToVConverter
 * @see com.curtisnewbie.module.ioc.convert.Converters
 */
public abstract class AbstractPropertyRegistry implements PropertyRegistry {

    protected final Map<String, String> propertyValues = new ConcurrentHashMap<>();

    @Override
    public String getProperty(String key) {
        return propertyValues.get(key);
    }

    @Override
    public void putProperty(String key, String value) {
        propertyValues.put(key, value);
    }

    @Override
    public boolean containsProperty(String key) {
        return propertyValues.containsKey(key);
    }
}
