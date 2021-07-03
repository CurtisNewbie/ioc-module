package com.curtisnewbie.module.ioc.beans.casees.invalid;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;
import org.junit.jupiter.api.Test;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithBoxedInteger {

    @Dependency
    private Integer intField;

    public Integer getIntField() {
        return intField;
    }

    public void setIntField(Integer intField) {
        this.intField = intField;
    }
}

