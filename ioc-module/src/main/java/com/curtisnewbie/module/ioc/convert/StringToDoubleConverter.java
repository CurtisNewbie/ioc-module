package com.curtisnewbie.module.ioc.convert;

/**
 * Converter for String-Double
 *
 * @author yongjie.zhuang
 */
public class StringToDoubleConverter implements Converter<String, Double> {

    @Override
    public Double convert(String s) {
        if (s == null)
            return null;
        return Double.parseDouble(s);
    }
}
