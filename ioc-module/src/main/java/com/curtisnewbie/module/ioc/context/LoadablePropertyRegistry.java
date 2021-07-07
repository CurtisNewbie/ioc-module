package com.curtisnewbie.module.ioc.context;

/**
 * Extension of {@link PropertyRegistry} that provides method to load properties in batch.
 *
 * @author yongjie.zhuang
 */
public interface LoadablePropertyRegistry extends PropertyRegistry {

    /**
     * Load properties (it's like initializing the PropertyRegistry)
     */
    void loadProperties();

}
