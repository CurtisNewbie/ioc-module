package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.aware.ApplicationContextAware;

/**
 * BeanPostProcessor for {@link ApplicationContextAware}
 *
 * @author yongjie.zhuang
 */
public class ContextAwareBeanPostProcessor implements BeanPostProcessor {

    private ApplicationContext applicationContext;

    public ContextAwareBeanPostProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBean(Object bean, String beanName) {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
        }
        return bean;
    }
}
