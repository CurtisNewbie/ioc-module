package com.curtisnewbie.demo;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithFurtherMaskedTommy implements HasName {

    @Dependency
    private FurtherMaskedTommy furtherMaskedTommy;

    public FurtherMaskedTommy getFurtherMaskedTommy() {
        return furtherMaskedTommy;
    }

    public void setFurtherMaskedTommy(FurtherMaskedTommy furtherMaskedTommy) {
        this.furtherMaskedTommy = furtherMaskedTommy;
    }

    @Override
    public String toString() {
        return "BeanWithFurtherMaskedTommy{" +
                "furtherMaskedTommy=" + furtherMaskedTommy +
                '}';
    }
}
