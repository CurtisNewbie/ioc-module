package com.curtisnewbie.module.ioc.demo;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class DummyBean {

    @Dependency
    private MaskedTommy maskedTommy;

    public MaskedTommy getMaskedTommy() {
        return maskedTommy;
    }

    public void setMaskedTommy(MaskedTommy maskedTommy) {
        this.maskedTommy = maskedTommy;
    }

    @Override
    public String toString() {
        return "DummyBean{" +
                "maskedTommy=" + maskedTommy +
                '}';
    }
}
