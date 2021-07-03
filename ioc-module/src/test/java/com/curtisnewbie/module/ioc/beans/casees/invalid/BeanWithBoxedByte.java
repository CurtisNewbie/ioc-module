package com.curtisnewbie.module.ioc.beans.casees.invalid;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithBoxedByte {

    @Dependency
    private Byte byteField;

    public Byte getByteField() {
        return byteField;
    }

    public void setByteField(Byte byteField) {
        this.byteField = byteField;
    }
}

