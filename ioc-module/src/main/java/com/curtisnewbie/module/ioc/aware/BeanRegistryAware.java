package com.curtisnewbie.module.ioc.aware;

import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.context.BeanRegistry;

/**
 * Type that is aware of the {@link com.curtisnewbie.module.ioc.context.BeanRegistry}
 *
 * @author yongjie.zhuang
 */
public interface BeanRegistryAware {

    /**
     * Set the bean registry
     */
    void setBeanRegistry(BeanRegistry beanRegistry);
}
