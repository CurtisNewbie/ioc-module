package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.aware.ApplicationContextAware;
import com.curtisnewbie.module.ioc.aware.BeanRegistryAware;
import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.context.BeanRegistry;

/**
 * BeanPostProcessor for {@link ApplicationContextAware}
 *
 * @author yongjie.zhuang
 */
public class BeanRegistryAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    private BeanRegistry beanRegistry;

    public BeanRegistryAwareBeanPostProcessor(BeanRegistry beanRegistry) {
        this.beanRegistry = beanRegistry;
    }

    @Override
    public Object postProcessBeanAfterInstantiation(Object bean, String beanName) {
        if (bean instanceof BeanRegistryAware) {
            ((BeanRegistryAware) bean).setBeanRegistry(beanRegistry);
        }
        return bean;
    }
}
