package com.curtisnewbie.module.ioc.context;

// TODO not implemented yet in dependency injection
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
