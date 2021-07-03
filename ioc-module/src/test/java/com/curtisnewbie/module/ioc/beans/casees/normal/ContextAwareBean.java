package com.curtisnewbie.module.ioc.beans.casees.normal;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.aware.ApplicationContextAware;
import com.curtisnewbie.module.ioc.aware.BeanRegistryAware;
import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.context.BeanRegistry;

/**
 * @author yongjie.zhuang
 */
@MBean
public class ContextAwareBean implements ApplicationContextAware, BeanRegistryAware {

    private ApplicationContext applicationContext;

    private BeanRegistry beanRegistry;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public BeanRegistry getBeanRegistry() {
        return beanRegistry;
    }

    @Override
    public void setBeanRegistry(BeanRegistry beanRegistry) {
        this.beanRegistry = beanRegistry;
    }
}
