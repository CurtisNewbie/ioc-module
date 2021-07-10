package com.curtisnewbie.module.ioc.convert;


/**
 * Converter for String-Short
 *
 * @author yongjie.zhuang
 */
public class StringToShortConverter implements Converter<String, Short> {

    @Override
    public Short convert(String s) {
        if (s == null)
            return null;
        return Short.parseShort(s);
    }
}
