package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating that context has been initialized, and should not be initialized again
 *
 * @author yongjie.zhuang
 */
public class ContextInitializedException extends ContextException {

    public ContextInitializedException() {
        super("Context can only be initialized for once");
    }

    public ContextInitializedException(String msg) {
        super(msg);
    }

    public ContextInitializedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
