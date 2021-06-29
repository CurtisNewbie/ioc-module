package com.curtisnewbie.module.ioc.beans.casees.circular;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class IndirectCircularDependencyServiceBImpl implements CircularDependentServiceB {

    @Dependency
    private CircularDependentServiceC circularDependentServiceC;

    public CircularDependentServiceC getCircularDependentServiceC() {
        return circularDependentServiceC;
    }

    public void setCircularDependentServiceC(CircularDependentServiceC circularDependentServiceC) {
        this.circularDependentServiceC = circularDependentServiceC;
    }
}
