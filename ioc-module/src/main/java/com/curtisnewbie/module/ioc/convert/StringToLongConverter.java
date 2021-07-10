package com.curtisnewbie.module.ioc.convert;

/**
 * Converter for String-Long
 *
 * @author yongjie.zhuang
 */
public class StringToLongConverter implements Converter<String, Long> {

    @Override
    public Long convert(String s) {
        if (s == null)
            return null;
        return Long.parseLong(s);
    }
}
