package com.curtisnewbie.module.ioc.context;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yongjie.zhuang
 */
public class DefaultBeanDefinitionRegistry extends DefaultBeanAliasRegistry implements BeanDefinitionRegistry {

    /**
     * beanName -> BeanDefinition
     */
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public boolean contains(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }
}
