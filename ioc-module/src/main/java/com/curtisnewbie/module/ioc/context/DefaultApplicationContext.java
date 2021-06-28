package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.util.BeanNameUtil;

import java.beans.Introspector;
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

    /** Scanner of classes of annotated beans */
    private final BeanClassScanner beanClzScanner;
    /** Resolver of beans' dependencies */
    private final BeanDependencyResolver dependencyResolver;
    /** Registry of singleton beans */
    private final SingletonBeanRegistry singletonBeanRegistry;

    public DefaultApplicationContext() {
        this.beanClzScanner = new AnnotatedBeanClassScanner();
        this.dependencyResolver = new AnnotatedBeanDependencyResolver();
        this.singletonBeanRegistry = new DefaultSingletonBeanRegistryImpl();
    }

    @Override
    protected void initializeContext() {
        // set of classes of beans that will be managed by this context
        Set<Class<?>> managedBeanClasses = beanClzScanner.scanBeanClasses(getClassLoader());
        // bean's name to bean's class
        Map<String, Class<?>> managedBeanMap = new HashMap<>();
        for (Class<?> c : managedBeanClasses) {
            if (c.isInterface()) {
                throw new IllegalStateException("Interface cannot be injected, type: " + c.toString());
            }
            managedBeanMap.put(BeanNameUtil.toBeanName(c), c);
        }
        Map<String, Set<String>> dependenciesMap = new HashMap<>();

        // load dependencies of each class
        for (Class<?> beanClazz : managedBeanClasses) {
            Set<String> dependencyBeanNames = dependencyResolver.resolveDependenciesNamesOfClass(beanClazz);
        }
    }

    @Override
    public BeanRegistry getBeanRegistry() {
        return singletonBeanRegistry;
    }
}
