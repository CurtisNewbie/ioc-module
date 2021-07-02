package com.curtisnewbie.module.ioc.context;

/**
 * Registry of singleton beans
 * <p>
 * Notice that, after the BeanRegistry being instantiated, the registry <b>should</b> be able to register singleton
 * beans, even though the {@link #loadBeanRegistry()} hasn't been invoked
 *
 * @author yongjie.zhuang
 */
public interface SingletonBeanRegistry extends ConfigurableBeanRegistry {

    /**
     * Register a singleton bean with the name
     * <p>
     * Notice that, after the BeanRegistry being instantiated, the registry <b>should</b> be able to register singleton
     * beans, even though the {@link #loadBeanRegistry()} hasn't been invoked
     *
     * @param beanName name of the bean
     * @param bean     the instance/bean
     */
    void registerSingletonBean(String beanName, Object bean);

    /**
     * Register a singleton bean with the class
     * <p>
     * Notice that, after the BeanRegistry being instantiated, the registry <b>should</b> be able to register singleton
     * beans, even though the {@link #loadBeanRegistry()} hasn't been invoked
     *
     * @param clazz class of the bean
     * @param bean  the instance/bean
     */
    void registerSingletonBean(Class<?> clazz, Object bean);

    /**
     * Register eagerly created singleton bean, whose dependencies are not yet resolved
     */
    void registerEagerlyCreatedSingletonBean(String beanName, Object bean);

    /**
     * Register a dependency of this bean
     */
    void registerDependency(String beanName, String dependentBeanName);

    /**
     * Check whether the given bean is dependent on the other bean (either directly or indirectly)
     *
     * @param beanName          given bean
     * @param dependentBeanName the bean that might be dependent on
     */
    boolean isDependent(String beanName, String dependentBeanName);

    /**
     * Check if the bean is resolved (instantiated and dependencies injected)
     *
     * @param beanName bean name
     */
    boolean isBeanResolved(String beanName);

    /**
     * Mark the bean as resolved (instantiated and dependencies injected)
     *
     * @param beanName bean name
     * @see #isBeanResolved(String)
     */
    void markBeanAsResolved(String beanName);

    /**
     * Load the bean registry
     * <p>
     * Which includes scanning beans' classes, instantiating beans and injecting their dependencies
     */
    void loadBeanRegistry();
}
