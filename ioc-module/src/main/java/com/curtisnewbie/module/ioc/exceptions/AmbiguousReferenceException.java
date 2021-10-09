package com.curtisnewbie.module.ioc.exceptions;


/**
 * Exception indicating the reference to a bean is ambiguous, there may be multiple beans, and it doesn't know which to
 * choose
 *
 * @author yongjie.zhuang
 */
public class AmbiguousReferenceException extends ContextException {

    public AmbiguousReferenceException() {
        super("Ambiguous reference, multiple instances might have been found");
    }

}
