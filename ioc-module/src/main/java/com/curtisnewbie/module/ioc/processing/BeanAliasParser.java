package com.curtisnewbie.module.ioc.processing;

import java.util.Set;

/**
 * Parser of bean's aliases
 *
 * @author yongjie.zhuang
 */
public interface BeanAliasParser {

    /**
     * Parse bean's aliases by class
     *
     * @param clazz bean's class
     */
    Set<String> parseBeanAliases(Class<?> clazz);
}

