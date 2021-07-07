package com.curtisnewbie.module.ioc.annotations;


import java.lang.annotation.*;

/**
 * Inject a property value, only String is supported so far
 *
 * @author yongjie.zhuang
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyValue {

    /**
     * Name of the property
     */
    String value();

    /**
     * Set whether the property is required, by default it's true
     */
    boolean required() default true;
}
