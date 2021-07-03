package com.curtisnewbie.module.ioc.beans.casees.invalid;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;
import org.junit.jupiter.api.Test;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithString {

    @Dependency
    private String strField;

    public String getStrField() {
        return strField;
    }

    public void setStrField(String strField) {
        this.strField = strField;
    }
}

