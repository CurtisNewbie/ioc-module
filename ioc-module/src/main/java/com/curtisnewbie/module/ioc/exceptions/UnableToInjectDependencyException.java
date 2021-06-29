package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating that the operation to inject dependency has failed for some reasons (e.g., writer method not
 * accessible)
 *
 * @author yongjie.zhuang
 */
public class UnableToInjectDependencyException extends DependencyException {

    public UnableToInjectDependencyException() {
        super();
    }

    public UnableToInjectDependencyException(String msg) {
        super(msg);
    }

    public UnableToInjectDependencyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
