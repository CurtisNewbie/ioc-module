package com.curtisnewbie.module.ioc.beans.casees.invalid;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithBoxedChar {

    @Dependency
    private Character charField;

    public Character getCharField() {
        return charField;
    }

    public void setCharField(Character charField) {
        this.charField = charField;
    }
}

