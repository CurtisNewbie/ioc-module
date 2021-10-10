package com.curtisnewbie.module.ioc.context;

import java.util.Set;

/**
 * @author yongjie.zhuang
 */
public interface BeanAliasRegistry {

    /**
     * Get bean's name by the name that is possibly an alias
     * <p>
     * If the {@code BeanAliasRegistry} doesn't know the given name, it will just return the name given, since we are
     * not sure whether the given name is actually an alias
     * </p>
     *
     * @param beanNameOrAlias bean name or its alias
     * @return bean's name
     */
    String getBeanName(String beanNameOrAlias);

    /**
     * Get one or more bean's name by the alias, it's possible that multiple bean shares the same alias
     * <p>
     * If the {@code BeanAliasRegistry} doesn't know the given name, it will just return the name given, since we are
     * not sure whether the given name is actually an alias
     * </p>
     *
     * @param beanNameOrAlias bean name or its alias
     * @return one or more beans' name
     */
    Set<String> getBeanNames(String beanNameOrAlias);

    /**
     * Add alias for bean
     *
     * @param beanName beanName
     * @param alias    alias
     */
    void addAlias(String beanName, String alias);
}
