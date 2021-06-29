package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating that circular dependencies has been found
 *
 * @author yongjie.zhuang
 */
public class CircularDependencyException extends DependencyException {

    public CircularDependencyException() {
        super("Circular dependencies not permitted");
    }

    public CircularDependencyException(String msg) {
        super(msg);
    }

    public CircularDependencyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
