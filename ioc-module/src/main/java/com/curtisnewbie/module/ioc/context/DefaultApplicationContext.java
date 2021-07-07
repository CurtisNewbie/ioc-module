package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.processing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of application context, which currently only supports singleton beans
 *
 * @author yongjie.zhuang
 */
public class DefaultApplicationContext extends AbstractApplicationContext {

    /** Registry of singleton beans */
    private final SingletonBeanRegistry singletonBeanRegistry;
    private List<BeanPostProcessor> beanPostProcessorList;
    private final BeanDependencyParser beanDependencyParser;
    private final BeanNameGenerator beanNameGenerator;
    private final BeanClassScanner beanClassScanner;
    private final BeanInstantiationStrategy beanInstantiationStrategy;
    private final BeanAliasParser beanAliasParser;

    public DefaultApplicationContext(
            BeanDependencyParser beanDependencyParser,
            BeanNameGenerator beanNameGenerator, BeanClassScanner beanClassScanner,
            BeanInstantiationStrategy beanInstantiationStrategy,
            BeanAliasParser beanAliasParser,
            List<BeanPostProcessor> extraBeanPostProcessors
    ) {
        this.beanDependencyParser = beanDependencyParser;
        this.beanNameGenerator = beanNameGenerator;
        this.beanClassScanner = beanClassScanner;
        this.beanInstantiationStrategy = beanInstantiationStrategy;
        this.beanAliasParser = beanAliasParser;
        // create bean registry
        this.singletonBeanRegistry = new DefaultSingletonBeanRegistry(
                this.beanClassScanner,
                this.beanNameGenerator,
                this.beanInstantiationStrategy,
                this.beanAliasParser
        );
        // create a list of bean post processors, note that this order matters
        this.beanPostProcessorList = Arrays.asList(
                new DependencyInjectionBeanPostProcessor(singletonBeanRegistry, beanDependencyParser),
                new ApplicationContextAwareBeanPostProcessor(this),
                new BeanRegistryAwareBeanPostProcessor(this.singletonBeanRegistry)
        );
        // add more bean post processors if provided
        if (extraBeanPostProcessors != null)
            this.beanPostProcessorList.addAll(extraBeanPostProcessors);

        // register bean post processors
        for (BeanPostProcessor bpp : beanPostProcessorList)
            this.singletonBeanRegistry.registerBeanPostProcessor(bpp);
    }

    @Override
    protected void initializeContext() {
        // mute the beanRegistry if necessary
        if (this.singletonBeanRegistry.canMuteLog() && isLogMuted())
            this.singletonBeanRegistry.muteLog();
        // register singleton beans that have been created, e.g., this ApplicationContext
        this.singletonBeanRegistry.registerSingletonBean(ApplicationContext.class, this);
        // starts bean scanning, instantiation, and dependency injection
        this.singletonBeanRegistry.loadBeanRegistry();
    }

    @Override
    public BeanRegistry getBeanRegistry() {
        return singletonBeanRegistry;
    }

    @Override
    public PropertyRegistry getPropertyRegistry() {
        return null;
    }

}
