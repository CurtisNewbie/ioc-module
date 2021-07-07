package com.curtisnewbie.module.ioc.convert;

/**
 * Converter for String-Double
 *
 * @author yongjie.zhuang
 */
public class StringToDoubleConverter implements StringToVConverter<Double> {

    @Override
    public Double convert(String s) {
        if (s == null)
            return null;
        return Double.parseDouble(s);
    }
}
