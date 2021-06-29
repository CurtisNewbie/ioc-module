package com.curtisnewbie.module.ioc.beans.casees.circular;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class DirectCircularDependencyServiceAImpl implements CircularDependentServiceA {

    @Dependency
    private CircularDependentServiceB circularDependentServiceB;

    public CircularDependentServiceB getCircularDependentServiceB() {
        return circularDependentServiceB;
    }

    public void setCircularDependentServiceB(CircularDependentServiceB circularDependentServiceB) {
        this.circularDependentServiceB = circularDependentServiceB;
    }
}
