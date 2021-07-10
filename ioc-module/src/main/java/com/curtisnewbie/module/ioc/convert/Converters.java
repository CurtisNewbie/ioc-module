package com.curtisnewbie.module.ioc.convert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that caches a number of {@link Converter}
 * <p>
 * Some common used converters are pre-populated, string -> int.
 * </p>
 * <p>
 * Can be extended with {@link #addConverter(Class, Class, Converter)}
 * </p>
 *
 * @author yongjie.zhuang
 */
public final class Converters {

    /**
     * outer layer: class (T class) to map of Converter(s) that converts from T class to ? class
     * <p>
     * inner layer: class (V class) to Converter that converts from ? to V class
     */
    private static final Map<Class<?>, Map<Class<?>, Converter<?, ?>>> fromToConverters = new ConcurrentHashMap<>();

    static {
        // pre-populate some of the common converters
        StringToIntegerConverter strToInt = new StringToIntegerConverter();
        addConverter(String.class, Integer.class, strToInt);
        addConverter(String.class, int.class, strToInt);

        StringToDoubleConverter strToDouble = new StringToDoubleConverter();
        addConverter(String.class, Double.class, strToDouble);
        addConverter(String.class, double.class, strToDouble);

        StringToLongConverter strToLong = new StringToLongConverter();
        addConverter(String.class, Long.class, strToLong);
        addConverter(String.class, long.class, strToLong);

        StringToShortConverter strToShort = new StringToShortConverter();
        addConverter(String.class, Short.class, strToShort);
        addConverter(String.class, short.class, strToShort);

        // special no-op converters
        addConverter(String.class, String.class, t -> t);
    }

    private Converters() {

    }

    /**
     * Check if there is a {@link Converter} that supports converting the  given types
     *
     * @param from from type (the T in {@code Converter<T,V>})
     * @param to   target type (the V in {@code Converter<T,V>})
     */
    public static boolean support(Class<?> from, Class<?> to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        return fromToConverters.containsKey(from) && fromToConverters.get(from).containsKey(to);
    }

    /**
     * Get {@link Converter} for the given types
     *
     * @param from from type (the T in {@code Converter<T,V>})
     * @param to   target type (the V in {@code Converter<T,V>})
     */
    public static <T, V> Converter<T, V> getConverter(Class<T> from, Class<V> to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Map<Class<?>, Converter<?, ?>> fromMap = fromToConverters.get(from);
        if (fromMap == null)
            return null;
        return (Converter<T, V>) fromMap.get(to);
    }

    /**
     * Add {@link Converter} for the given types
     * <p>
     * Method {@link #support(Class, Class)} should be invoked in advance to check if there is already a Converter
     * supports the given types, if there is a supported converted, the existing one will be replaced.
     * </p>
     *
     * @param from from type (the T in {@code Converter<T,V>})
     * @param to   target type (the V in {@code Converter<T,V>})
     */
    public static <T, V> void addConverter(Class<T> from, Class<V> to, Converter<T, V> converter) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Objects.requireNonNull(converter);
        synchronized (fromToConverters) {
            fromToConverters.computeIfAbsent(from, fc -> new ConcurrentHashMap<>());
            // replace the old one directly
            fromToConverters.get(from).put(to, converter);
        }
    }
}
