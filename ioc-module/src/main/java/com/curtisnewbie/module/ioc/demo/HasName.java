package com.curtisnewbie.module.ioc.demo;

/**
 * @author yongjie.zhuang
 */
public interface HasName {

    default void sayName() {
        System.out.println("I am: " + toString());
    }

}
