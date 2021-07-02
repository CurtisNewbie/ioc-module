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

    /** Registry of singleton beans */
    private final SingletonBeanRegistry singletonBeanRegistry;
    private List<BeanPostProcessor> beanPostProcessorList;
    private final BeanDependencyParser beanDependencyParser;

    public DefaultApplicationContext() {
        this.singletonBeanRegistry = new DefaultSingletonBeanRegistry();
        this.beanDependencyParser = new AnnotatedBeanDependencyParser();
        // this order matters
        this.beanPostProcessorList = Arrays.asList(
                new DependencyAnnotationBeanPostProcessor(singletonBeanRegistry, beanDependencyParser),
                new ContextAwareBeanPostProcessor(this)
        );
    }

    @Override
    protected void initializeContext() {
        // register bean post processors
        this.singletonBeanRegistry.registerBeanPostProcessor(beanPostProcessorList);
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
    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Objects.requireNonNull(beanPostProcessor);
        this.beanPostProcessorList.add(beanPostProcessor);
    }
}
