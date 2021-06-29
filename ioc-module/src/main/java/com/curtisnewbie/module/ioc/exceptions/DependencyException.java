package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception related to dependencies
 *
 * @author yongjie.zhuang
 */
public abstract class DependencyException extends ContextException {

    public DependencyException() {
        super();
    }

    public DependencyException(String msg) {
        super(msg);
    }

    public DependencyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
