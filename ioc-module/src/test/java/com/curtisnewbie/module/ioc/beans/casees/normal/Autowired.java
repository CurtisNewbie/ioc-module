package com.curtisnewbie.module.ioc.beans.casees.normal;

import com.curtisnewbie.module.ioc.annotations.Dependency;

import java.lang.annotation.*;

/**
 * @author yongjie.zhuang
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Dependency
public @interface Autowired {
}
