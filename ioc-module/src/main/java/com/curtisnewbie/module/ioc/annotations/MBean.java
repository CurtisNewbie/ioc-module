package com.curtisnewbie.module.ioc.annotations;

import java.lang.annotation.*;

/**
 * Indicating that this class should be managed as a bean by the container, it might also be injected into another bean
 * as a dependency
 *
 * @author yongjie.zhuang
 * @see Dependency
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface MBean {
}
