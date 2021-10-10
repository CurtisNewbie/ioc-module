package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.exceptions.AmbiguousReferenceException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author yongjie.zhuang
 */
public class DefaultBeanAliasRegistry implements BeanAliasRegistry {

    /**
     * Alias map, bean alias -> actual bean names
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

    /**
     * Try to resolve the true bean name of the given name
     * <p>
     * If the {@code beanAliasMap} doesn't contain any values for this name, the name may not be an alias, we just
     * return it
     * </p>
     * <p>
     * Else, we return any bean names that this alias point to, the returned {@code Set} is never null
     * </p>
     *
     * @param beanAlias alias or beanName
     * @return Set containing the beanName that this alias is associated with
     */
    private Set<String> findNamesOfPossibleBeanAlias(String beanAlias) {
        Objects.requireNonNull(beanAlias);

        Set<String> actualBeanNames;

        // no alias found, it's possible that it's not an alias at all, so we just return it
        if (!beanAliasMap.containsKey(beanAlias) || (actualBeanNames = beanAliasMap.get(beanAlias)).isEmpty()) {
            return Collections.singleton(beanAlias);
        }
        // actualBeanNames will not be null, since we checked whether the map contains the given key
        return actualBeanNames;
    }
}
