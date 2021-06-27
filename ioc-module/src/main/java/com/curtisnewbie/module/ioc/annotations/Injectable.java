package com.curtisnewbie.module.ioc.annotations;

import java.lang.annotation.*;

/**
 * Indicating that this class should be managed as a injectable bean
 *
 * @author yongjie.zhuang
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Injectable {
}
