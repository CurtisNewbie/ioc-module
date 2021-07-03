package com.curtisnewbie.module.ioc.context;

/**
 * Context of the application
 *
 * @author yongjie.zhuang
 */
public interface ApplicationContext {

    /**
     * Get bean registry
     */
    BeanRegistry getBeanRegistry();

    /**
     * Get main class
     */
    Class<?> getMainClazz();
}
