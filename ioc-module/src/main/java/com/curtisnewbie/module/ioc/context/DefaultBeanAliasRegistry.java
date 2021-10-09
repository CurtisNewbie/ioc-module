package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.exceptions.AmbiguousReferenceException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author yongjie.zhuang
 */
public class DefaultBeanAliasRegistry implements BeanAliasRegistry {

    /**
     * Set of beans' name (excluding aliases)
     * <br>
     * This set will only include those (the implementation beans) that are managed by this registry, their interfaces
     * will not be included here.
     *
     * @see #beanAliasMap
     */
    protected final Set<String> beanNameSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

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

        // first check if this beanName is actually an alias
        if (beanNameSet.contains(beanAlias)) {
            // it's the beanName already, it's not alias
            return Collections.singleton(beanAlias);
        }

        // this bean name is an alias, see if it's pointing to some bean
        Set<String> actualBeanNames = beanAliasMap.get(beanAlias);
        if (actualBeanNames == null || actualBeanNames.isEmpty())
            return null;
        // multiple beans are found, must have circular dependencies
//        if (actualBeanNames.size() > 1)
//            throw new CircularDependencyException(format("Found two beans (%s) with the same alias (%s)",
//                    actualBeanNames.toString(), beanAlias));
        return actualBeanNames;
    }
}
