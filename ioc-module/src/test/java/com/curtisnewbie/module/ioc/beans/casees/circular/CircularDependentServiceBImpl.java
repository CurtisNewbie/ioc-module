package com.curtisnewbie.module.ioc.beans.casees.circular;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class CircularDependentServiceBImpl implements CircularDependentServiceB {

    @Dependency
    private CircularDependentServiceA circularDependentServiceA;

    public CircularDependentServiceA getCircularDependentServiceA() {
        return circularDependentServiceA;
    }

    public void setCircularDependentServiceA(CircularDependentServiceA circularDependentServiceA) {
        this.circularDependentServiceA = circularDependentServiceA;
    }
}
