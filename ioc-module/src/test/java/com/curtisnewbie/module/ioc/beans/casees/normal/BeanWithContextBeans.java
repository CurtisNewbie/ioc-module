package com.curtisnewbie.module.ioc.beans.casees.normal;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.context.BeanRegistry;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithContextBeans {

    @Dependency
    private ApplicationContext applicationContext;

    @Dependency
    private BeanRegistry beanRegistry;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public BeanRegistry getBeanRegistry() {
        return beanRegistry;
    }

    public void setBeanRegistry(BeanRegistry beanRegistry) {
        this.beanRegistry = beanRegistry;
    }
}
