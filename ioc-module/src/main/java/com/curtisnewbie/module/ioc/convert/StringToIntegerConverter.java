package com.curtisnewbie.module.ioc.convert;

/**
 * Converter for String-Integer
 *
 * @author yongjie.zhuang
 */
public class StringToIntegerConverter implements StringToVConverter<Integer> {

    @Override
    public Integer convert(String s) {
        if (s == null)
            return null;
        return Integer.parseInt(s);
    }
}
