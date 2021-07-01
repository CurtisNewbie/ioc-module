package com.curtisnewbie.module.ioc.aware;

import com.curtisnewbie.module.ioc.context.ApplicationContext;

/**
 * Type that is aware of the {@link ApplicationContext}
 *
 * @author yongjie.zhuang
 */
public interface ApplicationContextAware {

    /**
     * Set application context
     */
    void setApplicationContext(ApplicationContext applicationContext);
}
