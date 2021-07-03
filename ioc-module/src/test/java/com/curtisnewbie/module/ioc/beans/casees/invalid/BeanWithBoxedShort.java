package com.curtisnewbie.module.ioc.beans.casees.invalid;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;
import org.junit.jupiter.api.Test;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithBoxedShort {

    @Dependency
    private Short shortField;

    public Short getShortField() {
        return shortField;
    }

    public void setShortField(Short shortField) {
        this.shortField = shortField;
    }
}

