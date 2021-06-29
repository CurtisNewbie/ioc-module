package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating that the singleton bean has been registered
 *
 * @author yongjie.zhuang
 */
public class SingletonBeanRegisteredException extends ContextException {

    public SingletonBeanRegisteredException() {
        super("Singleton bean has been registered");
    }

    public SingletonBeanRegisteredException(String beanName) {
        super("Singleton bean " + beanName + " has been registered");
    }

    public SingletonBeanRegisteredException(String beanName, Throwable cause) {
        super("Singleton bean " + beanName + " has been registered", cause);
    }
}
