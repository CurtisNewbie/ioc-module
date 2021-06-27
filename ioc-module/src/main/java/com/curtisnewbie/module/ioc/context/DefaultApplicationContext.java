package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.annotations.AnnotatedBeanResolver;
import com.curtisnewbie.module.ioc.annotations.AnnotatedBeanResolverImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Default implementation of application context, which currently only supports singleton beans
 *
 * @author yongjie.zhuang
 */
public class DefaultApplicationContext extends AbstractApplicationContext {

    /** Registry of singleton beans */
    private final SingletonBeanRegistry singletonBeanRegistry = new SingletonBeanRegistryImpl();
    /** Resolver of annotated beans */
    private final AnnotatedBeanResolver beanResolver = new AnnotatedBeanResolverImpl();

    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz should not be null");
        return singletonBeanRegistry.getBeanByClass(clazz);
    }

    @Override
    public Object getBeanByName(String beanName) {
        Objects.requireNonNull(beanName, "bean name should not be null");
        return singletonBeanRegistry.getBeanByName(beanName);
    }

    @Override
    protected void initializeContext() {
        // set of classes of beans that will be managed by this context
        Set<Class<?>> managedBeanClasses = beanResolver.resolveBeanClasses(getClassLoader());
        Map<Class<?>, Set<Class<?>>> dependenciesMap = new HashMap<>();

        // load dependencies of each class
        for (Class<?> beanClazz : managedBeanClasses) {
            dependenciesMap.put(beanClazz, beanResolver.resolveDependenciesOfClass(beanClazz));
        }
    }
}
