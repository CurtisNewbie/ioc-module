package com.curtisnewbie.module.ioc.exceptions;

/**
 * Exception related to the context of this application
 *
 * @author yongjie.zhuang
 */
public abstract class ContextException extends Exception {

    ContextException() {

    }

    ContextException(String msg) {
        super(msg);
    }

    ContextException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
