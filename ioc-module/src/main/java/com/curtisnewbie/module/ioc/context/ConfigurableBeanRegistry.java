package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.context.processing.BeanDependencyParser;
import com.curtisnewbie.module.ioc.context.processing.BeanInstantiationStrategy;
import com.curtisnewbie.module.ioc.context.processing.BeanPostProcessor;
import com.curtisnewbie.module.ioc.context.processing.BeanClassScanner;

import java.util.List;

/**
 * Bean registry that is configurable
 *
 * @author yongjie.zhuang
 */
public interface ConfigurableBeanRegistry extends BeanRegistry {

    /**
     * Register a list of {@link BeanPostProcessor}
     */
    void registerBeanPostProcessor(List<BeanPostProcessor> beanPostProcessorList);

    /**
     * Set {@link BeanDependencyParser} to be used by the registry
     */
    void setBeanDependencyParser(BeanDependencyParser beanDependencyParser);

    /**
     * Set {@link BeanClassScanner} to be used by the registry
     */
    void setBeanClassScanner(BeanClassScanner beanClassScanner);

    /**
     * Set {@link BeanInstantiationStrategy} to be used by the registry
     */
    void setBeanInstantiationStrategy(BeanInstantiationStrategy beanInstantiationStrategy);

}
