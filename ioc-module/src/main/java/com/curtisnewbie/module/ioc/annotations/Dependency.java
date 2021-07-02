package com.curtisnewbie.module.ioc.annotations;

import java.lang.annotation.*;

/**
 * Mark a bean as a dependency that should be injected by the container
 *
 * @author yongjie.zhuang
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Dependency {
}
