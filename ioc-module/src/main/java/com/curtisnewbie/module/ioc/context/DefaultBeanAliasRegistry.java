package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.exceptions.AmbiguousReferenceException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author yongjie.zhuang
 */
public class DefaultBeanAliasRegistry implements BeanAliasRegistry {

    /**
     * Alias map; bean alias (e.g., interfaces) to a set of actual bean names
     * <br>
     */
    protected final Map<String, Set<String>> beanAliasMap = new ConcurrentHashMap<>();

    @Override
    public String getBeanName(String beanNameOrAlias) {
        Set<String> names = findNamesOfPossibleBeanAlias(beanNameOrAlias);
        if (names.isEmpty())
            return null;
        int size = names.size();
        if (size > 1) {
            throw new AmbiguousReferenceException();
        }
        // only return the first one
        return names.iterator().next();
    }

    @Override
    public Set<String> getBeanNames(String beanNameOrAlias) {
        return findNamesOfPossibleBeanAlias(beanNameOrAlias);
    }

    @Override
    public void addAlias(String beanName, String alias) {
        beanAliasMap.computeIfAbsent(alias, k -> new HashSet<>());
        beanAliasMap.get(alias).add(beanName);
    }

    private Set<String> findNamesOfPossibleBeanAlias(String beanAlias) {
        Objects.requireNonNull(beanAlias);

        Set<String> actualBeanNames;

        // no alias found, it's possible that it's not an alias at all, so we just return it
        if (!beanAliasMap.containsKey(beanAlias) || (actualBeanNames = beanAliasMap.get(beanAlias)).isEmpty()) {
            return Collections.singleton(beanAlias);
        }

        if (actualBeanNames == null)
            actualBeanNames = Collections.emptySet();
        return actualBeanNames;
    }
}
