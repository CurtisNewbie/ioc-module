package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.processing.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of application context, which currently only supports singleton beans
 *
 * @author yongjie.zhuang
 */
public class DefaultApplicationContext extends AbstractApplicationContext {

    /** bean registry */
    private final DefaultInjectCapableBeanRegistry beanRegistry;
    private List<BeanPostProcessor> beanPostProcessors;
    private final BeanDependencyParser beanDependencyParser;
    private final BeanNameGenerator beanNameGenerator;
    private final BeanClassScanner beanClassScanner;
    private final BeanInstantiationStrategy beanInstantiationStrategy;
    private final BeanAliasParser beanAliasParser;
    private final PropertyRegistry propertyRegistry;

    public DefaultApplicationContext(
            BeanDependencyParser beanDependencyParser,
            BeanNameGenerator beanNameGenerator, BeanClassScanner beanClassScanner,
            BeanInstantiationStrategy beanInstantiationStrategy,
            BeanAliasParser beanAliasParser,
            List<BeanPostProcessor> extraBeanPostProcessors,
            PropertyRegistry propertyRegistry
    ) {
        checkNonNull(beanDependencyParser,
                beanNameGenerator,
                beanClassScanner,
                beanInstantiationStrategy,
                beanAliasParser,
                propertyRegistry
        );
        this.beanDependencyParser = beanDependencyParser;
        this.beanNameGenerator = beanNameGenerator;
        this.beanClassScanner = beanClassScanner;
        this.beanInstantiationStrategy = beanInstantiationStrategy;
        this.beanAliasParser = beanAliasParser;
        this.propertyRegistry = propertyRegistry;

        if (propertyRegistry instanceof RefreshableRegistry)
            ((RefreshableRegistry) propertyRegistry).refresh();

        // create bean registry
        this.beanRegistry = new DefaultInjectCapableBeanRegistry(
                this.beanClassScanner,
                this.beanNameGenerator,
                this.beanInstantiationStrategy,
                this.beanAliasParser
        );
        // create a list of bean post processors, note that this order matters
        this.beanPostProcessors = Arrays.asList(
                new DependencyInjectionBeanPostProcessor(beanRegistry, beanDependencyParser),
                new PropertyValueBeanPostProcessor(propertyRegistry),
                new ApplicationContextAwareBeanPostProcessor(this),
                new BeanRegistryAwareBeanPostProcessor(this.beanRegistry)
        );
        // add more bean post processors if provided
        if (extraBeanPostProcessors != null)
            this.beanPostProcessors.addAll(extraBeanPostProcessors);

        // register bean post processors
        registerBeanPostProcessors();
    }

    private void registerBeanPostProcessors() {
        for (BeanPostProcessor bpp : beanPostProcessors)
            this.beanRegistry.registerBeanPostProcessor(bpp);
    }

    @Override
    protected void initializeContext() {
        // mute the beanRegistry if necessary
        if (this.beanRegistry.canMuteLog() && isLogMuted())
            this.beanRegistry.muteLog();
        // register singleton beans that have been created, e.g., this ApplicationContext
        this.beanRegistry.registerSingletonBean(ApplicationContext.class, this);
        // starts bean scanning, instantiation, and dependency injection
        this.beanRegistry.refresh();
    }

    @Override
    public BeanRegistry getBeanRegistry() {
        return beanRegistry;
    }

    @Override
    public PropertyRegistry getPropertyRegistry() {
        return null;
    }

    private void checkNonNull(Object... objs) {
        for (Object o : objs) {
            Objects.requireNonNull(o);
        }
    }

}
