package com.curtisnewbie.module.ioc.exceptions;

/**
 * Exception indicating that the singleton bean has been registered
 *
 * @author yongjie.zhuang
 */
public class SingletonBeanRegistered extends Exception {

    public SingletonBeanRegistered() {
        super("Singleton bean has been registered");
    }

    public SingletonBeanRegistered(String msg) {
        super(msg);
    }

    public SingletonBeanRegistered(String msg, Throwable cause) {
        super(msg, cause);
    }
}
