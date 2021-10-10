package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating that the bean is not found
 *
 * @author yongjie.zhuang
 */
public class BeanNotFoundException extends ContextException {

    public BeanNotFoundException() {
        super("Bean is not found");
    }

    public BeanNotFoundException(String msg) {
        super(msg);
    }

    public BeanNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public static BeanNotFoundException forBeanName(String beanName) {
        return new BeanNotFoundException(String.format("Bean '%s' is not found", beanName));
    }
}
