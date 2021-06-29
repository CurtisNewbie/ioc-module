package com.curtisnewbie.module.ioc.context;

import java.util.Arrays;
import java.util.List;

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
        this.singletonBeanRegistry.registerBeanPostProcessor(beanPostProcessorList);
        this.singletonBeanRegistry.loadBeanRegistry();
    }

    @Override
    public BeanRegistry getBeanRegistry() {
        return singletonBeanRegistry;
    }
}
