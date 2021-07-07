package com.curtisnewbie.module.ioc.context;

/**
 * Extension of {@link PropertyRegistry} that provides method to load properties in batch.
 *
 * @author yongjie.zhuang
 */
public interface LoadablePropertyRegistry extends PropertyRegistry {

    /**
     * Scan the classpath, load property files in resource folder
     */
    void loadResourceProperties();

}
