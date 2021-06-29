package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating that the creation of bean failed for some reasons
 *
 * @author yongjie.zhuang
 */
public class BeanCreationException extends ContextException {

    public BeanCreationException() {
        super("Bean cannot be created");
    }

    public BeanCreationException(String msg) {
        super(msg);
    }

    public BeanCreationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
