package com.curtisnewbie.module.ioc.beans.casees.normal;

/**
 * @author yongjie.zhuang
 */
public interface KnowWhoIAm {

    default void whoIAm() {
        System.out.println("I am: " + toString());
    }

}
