package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.processing.*;

/**
 * Configurable Initializer of the context
 * <p>
 * Note that invoking the {@code register*} methods will override the default implementation, and these methods must be
 * invoked before {@link #initialize(Class)}
 * </p>
 *
 * @author yongjie.zhuang
 * @see ApplicationContextFactory
 */
public interface ConfigurableContextInitializer extends ContextInitializer {

    /**
     * Initialize the context
     *
     * @param mainClazz the class that contains the main(...) method
     * @return ApplicationContext
     */
    ApplicationContext initialize(Class<?> mainClazz);

    /**
     * Register a {@link BeanPostProcessor} to be used by the context
     */
    void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * Register a {@link BeanDependencyParser} to be used by the context
     */
    void registerBeanDependencyParser(BeanDependencyParser beanDependencyParser);

    /**
     * Register a {@link BeanNameGenerator} to be used by the context
     */
    void registerBeanNameGenerator(BeanNameGenerator beanNameGenerator);

    /**
     * Register a {@link BeanClassScanner} to be used by the context
     */
    void registerBeanClassScanner(BeanClassScanner beanClassScanner);

    /**
     * Register a {@link BeanInstantiationStrategy} to be used by the context
     */
    void registerBeanInstantiationStrategy(BeanInstantiationStrategy beanInstantiationStrategy);

    /**
     * Register a {@link BeanAliasParser} to be used by the context
     */
    void registerBeanAliasParser(BeanAliasParser beanAliasParser);

}

