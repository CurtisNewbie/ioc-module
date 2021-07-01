package com.curtisnewbie.demo;

import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class TommyBean implements MaskedTommy {

    @Override
    public String toString() {
        return "tommy bean";
    }
}
