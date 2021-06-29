package com.curtisnewbie.module.ioc.beans.casees.circular;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class IndirectCircularDependencyServiceCImpl implements CircularDependentServiceC {

    @Dependency
    private CircularDependentServiceA circularDependentServiceA;

    public CircularDependentServiceA getCircularDependentServiceA() {
        return circularDependentServiceA;
    }

    public void setCircularDependentServiceA(CircularDependentServiceA circularDependentServiceA) {
        this.circularDependentServiceA = circularDependentServiceA;
    }
}
