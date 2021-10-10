package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.aware.ApplicationContextAware;
import com.curtisnewbie.module.ioc.context.BeanDefinition;

/**
 * BeanPostProcessor for {@link ApplicationContextAware}
 *
 * @author yongjie.zhuang
 */
public class ApplicationContextAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    private ApplicationContext applicationContext;

    public ApplicationContextAwareBeanPostProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
        }
        return bean;
    }
}
