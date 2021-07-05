package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.config.LogMutable;
import com.curtisnewbie.module.ioc.processing.*;

import java.util.List;

/**
 * Bean registry that is configurable
 *
 * @author yongjie.zhuang
 */
public interface ConfigurableSingletonBeanRegistry extends LogMutable, SingletonBeanRegistry {

    /**
     * Register a list of {@link BeanPostProcessor}
     */
    void registerBeanPostProcessor(List<BeanPostProcessor> beanPostProcessorList);

    /**
     * Set {@link BeanClassScanner} to be used by the registry
     */
    void setBeanClassScanner(BeanClassScanner beanClassScanner);

    /**
     * Set {@link BeanInstantiationStrategy} to be used by the registry
     */
    void setBeanInstantiationStrategy(BeanInstantiationStrategy beanInstantiationStrategy);

    /**
     * Set {@link BeanNameGenerator} to be used by the registry
     */
    void setBeanNameGenerator(BeanNameGenerator beanNameGenerator);

    /**
     * Set {@link BeanAliasParser} to be used by the register
     */
    void setBeanAliasParser(BeanAliasParser beanAliasParser);

    /**
     * Get classloader used by this bean registry
     */
    ClassLoader getClassLoader();

    /**
     * Get {@link BeanClassScanner} used by this registry
     */
    BeanClassScanner getBeanClzScanner();

    /**
     * Get {@link BeanNameGenerator} used by this registry
     */
    BeanNameGenerator getBeanNameGenerator();

    /**
     * Get {@link BeanInstantiationStrategy} used by this registry
     */
    BeanInstantiationStrategy getBeanInstantiationStrategy();

    /**
     * Get {@link BeanAliasParser} used by this registry
     */
    BeanAliasParser getBeanAliasParser();

}
