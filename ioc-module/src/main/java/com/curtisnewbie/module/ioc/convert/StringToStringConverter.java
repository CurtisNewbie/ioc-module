package com.curtisnewbie.module.ioc.convert;

/**
 * Noop Converter for String-String
 *
 * @author yongjie.zhuang
 */
public class StringToStringConverter implements StringToVConverter<String> {

    @Override
    public String convert(String s) {
        return s;
    }
}
