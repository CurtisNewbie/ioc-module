package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.processing.BeanPostProcessor;
import com.curtisnewbie.module.ioc.processing.ContextAwareBeanPostProcessor;

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
    private List<BeanPostProcessor> beanPostProcessorList = Arrays.asList(
            new ContextAwareBeanPostProcessor(this)
    );

    public DefaultApplicationContext() {
        this.singletonBeanRegistry = new DefaultSingletonBeanRegistry();
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
