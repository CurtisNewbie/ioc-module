package com.curtisnewbie.module.ioc.convert;

import java.util.*;

/**
 * Class that caches a number of {@link StringToVConverter}
 *
 * @author yongjie.zhuang
 */
public final class Converters {

    private static final Map<Class<?>, StringToVConverter<?>> supportedConverters;

    static {
        Map<Class<?>, StringToVConverter<?>> tempMap = new HashMap<>();
        tempMap.put(Integer.class, new StringToIntegerConverter());
        tempMap.put(Double.class, new StringToDoubleConverter());
        tempMap.put(Long.class, new StringToLongConverter());
        tempMap.put(Short.class, new StringToShortConverter());
        tempMap.put(String.class, new StringToStringConverter());
        supportedConverters = Collections.unmodifiableMap(tempMap);
    }

    private Converters() {
    }

    /**
     * Check if there is a {@link Converter} for the given type
     *
     * @param targetType target type (the V in {@code Converter<T,V>})
     */
    public static boolean support(Class targetType) {
        Objects.requireNonNull(targetType);
        return supportedConverters.containsKey(targetType);
    }

    /**
     * Get {@link StringToVConverter} for the given type
     */
    public static StringToVConverter<?> getPropertyConverter(Class<?> targetType) {
        Objects.requireNonNull(targetType);
        return supportedConverters.get(targetType);
    }
}
