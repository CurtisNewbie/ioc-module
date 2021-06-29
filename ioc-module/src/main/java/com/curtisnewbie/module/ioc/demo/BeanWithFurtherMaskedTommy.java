package com.curtisnewbie.module.ioc.demo;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithFurtherMaskedTommy {

    @Dependency
    private FurtherMaskedTommy furtherMaskedTommy;

    public FurtherMaskedTommy getFurtherMaskedTommy() {
        return furtherMaskedTommy;
    }

    public void setFurtherMaskedTommy(FurtherMaskedTommy furtherMaskedTommy) {
        this.furtherMaskedTommy = furtherMaskedTommy;
    }
}
