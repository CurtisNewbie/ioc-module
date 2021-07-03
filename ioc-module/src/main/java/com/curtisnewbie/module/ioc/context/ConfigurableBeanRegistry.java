package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.config.LogMutable;
import com.curtisnewbie.module.ioc.processing.BeanDependencyParser;
import com.curtisnewbie.module.ioc.processing.BeanInstantiationStrategy;
import com.curtisnewbie.module.ioc.processing.BeanPostProcessor;
import com.curtisnewbie.module.ioc.processing.BeanClassScanner;

import java.util.List;

/**
 * Bean registry that is configurable
 *
 * @author yongjie.zhuang
 */
public interface ConfigurableBeanRegistry extends BeanRegistry, LogMutable {

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

}
