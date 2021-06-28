package com.curtisnewbie.module.ioc.context;

/**
 * Default implementation of application context, which currently only supports singleton beans
 *
 * @author yongjie.zhuang
 */
public class DefaultApplicationContext extends AbstractApplicationContext {

    /** Registry of singleton beans */
    private final SingletonBeanRegistry singletonBeanRegistry;

    public DefaultApplicationContext() {
        this.singletonBeanRegistry = new DefaultSingletonBeanRegistryImpl(getClassLoader());
    }

    @Override
    protected void initializeContext() {
        this.singletonBeanRegistry.loadBeanRegistry();
    }

    @Override
    public BeanRegistry getBeanRegistry() {
        return singletonBeanRegistry;
    }
}
