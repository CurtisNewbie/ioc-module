package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating that the required dependency is unsatisfied
 *
 * @author yongjie.zhuang
 */
public class UnsatisfiedDependencyException extends DependencyException {

    public UnsatisfiedDependencyException() {
        super("Required dependency is unsatisfied");
    }

    public UnsatisfiedDependencyException(String msg) {
        super(msg);
    }

    public UnsatisfiedDependencyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
