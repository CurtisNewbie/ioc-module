package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.processing.*;
import com.curtisnewbie.module.ioc.exceptions.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of {@link SingletonBeanRegistry}
 *
 * @author yongjie.zhuang
 */
public class DefaultSingletonBeanRegistry extends DefaultBeanDefinitionRegistry implements SingletonBeanRegistry {

    /**
     * Set of beans' name (excluding aliases)
     * <br>
     * This set will only include those (the implementation beans) that are managed by this registry, their interfaces
     * will not be included here.
     *
     * @see #beanAliasMap
     */
    protected final Set<String> beanNameSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Beans map; bean name to bean instance
     * <p>
     * If the bean has multiple interfaces, the bean name will always be the name of the implementation bean, the
     * interface names will be the bean's alias
     *
     * @see #beanAliasMap
     */
    private final Map<String, Object> beanInstanceMap = new ConcurrentHashMap<>();

    /** mutex lock */
    private final Object mutex = new Object();

    /** generator of bean's name */
    protected final BeanNameGenerator beanNameGenerator;

    public DefaultSingletonBeanRegistry(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    @Override
    public void registerSingletonBean(String beanName, Object bean) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(bean);

        synchronized (getMutex()) {
            if (beanInstanceMap.get(beanName) != null) {
                throw new SingletonBeanRegisteredException(beanName);
            }
            beanInstanceMap.put(beanName, bean);
            beanNameSet.add(beanName);
        }
    }

    @Override
    public void registerSingletonBean(Class<?> clazz, Object bean) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(bean);

        String beanName = beanNameGenerator.generateBeanName(clazz);
        registerSingletonBean(beanName, bean);
    }

    @Override
    public Map<String, Object> getBeansOfType(Class<?> parentType) {
        Objects.requireNonNull(parentType);

        Map<String, Object> beanNameToObj = new HashMap<>();
        synchronized (getMutex()) {
            // if the requested type itself is a implementation bean
            String parentBeanName = beanNameGenerator.generateBeanName(parentType);
            Object parentDirectImpl = beanInstanceMap.get(parentBeanName);
            if (parentDirectImpl != null)
                beanNameToObj.put(parentBeanName, parentDirectImpl);

            // try to find if any other beans that extend or implement this type
            Set<String> childBeans = getBeanNames(parentBeanName);
            if (childBeans != null) {
                for (String childName : childBeans) {
                    Object childObj = beanInstanceMap.get(childName);
                    Objects.requireNonNull(childObj);
                    if (childObj != parentDirectImpl) {
                        beanNameToObj.put(childName, childObj);
                    }
                }
            }
            return beanNameToObj;
        }
    }

    @Override
    public boolean containsBean(String name) {
        Objects.requireNonNull(name);
        String implBeanName = getBeanName(name);
        if (implBeanName == null)
            return false;
        return beanInstanceMap.containsKey(implBeanName);
    }

    @Override
    public boolean containsBean(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return containsBean(generateBeanName(clazz));
    }

    @Override
    public <T> T getBeanByClass(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.cast(getBeanByName(generateBeanName(clazz)));
    }

    @Override
    public Object getBeanByName(String aliasOrName) {
        Objects.requireNonNull(aliasOrName);
        final String beanName = getBeanName(aliasOrName);
        if (beanName == null)
            throw BeanNotFoundException.forBeanName(aliasOrName);

        // check if the instance exists, it may be manually registered or instantiated before
        Object bean = beanInstanceMap.get(beanName);
        return bean;
    }

    protected String generateBeanName(Class<?> clazz) {
        return beanNameGenerator.generateBeanName(clazz);
    }

    /** Get object for mutex lock */
    protected Object getMutex() {
        return this.mutex;
    }
}
