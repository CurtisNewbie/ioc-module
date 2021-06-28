package com.curtisnewbie.module.ioc.demo;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class DummyBean {

    @Dependency
    private TommyBean maskedTommy;

    public TommyBean getMaskedTommy() {
        return maskedTommy;
    }

    public void setMaskedTommy(TommyBean maskedTommy) {
        this.maskedTommy = maskedTommy;
    }

    @Override
    public String toString() {
        return "DummyBean{" +
                "maskedTommy=" + maskedTommy +
                '}';
    }
}
