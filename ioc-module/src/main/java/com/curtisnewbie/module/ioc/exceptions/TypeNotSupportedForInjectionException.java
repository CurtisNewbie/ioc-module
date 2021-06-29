package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating that type of the dependent bean is not support
 *
 * @author yongjie.zhuang
 */
public class TypeNotSupportedForInjectionException extends ContextException {

    public TypeNotSupportedForInjectionException() {
        super("Type not supported for dependency injection");
    }

    public TypeNotSupportedForInjectionException(String msg) {
        super(msg);
    }

    public TypeNotSupportedForInjectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
