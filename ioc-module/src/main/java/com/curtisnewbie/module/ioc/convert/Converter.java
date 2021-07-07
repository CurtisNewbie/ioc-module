package com.curtisnewbie.module.ioc.convert;

/**
 * A converter that converts {@code T} type to {@code V} type
 *
 * @author yongjie.zhuang
 */
public interface Converter<T, V> {

    /**
     * Convert value
     * @param t
     */
    V convert(T t);
}
