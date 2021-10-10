package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.config.LogMutable;
import com.curtisnewbie.module.ioc.exceptions.BeanNotFoundException;
import com.curtisnewbie.module.ioc.exceptions.ContextInitializedException;
import com.curtisnewbie.module.ioc.processing.BeanNameGenerator;
import com.curtisnewbie.module.ioc.processing.BeanPostProcessor;
import com.curtisnewbie.module.ioc.processing.InstantiationAwareBeanPostProcessor;
import com.curtisnewbie.module.ioc.util.LogUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static com.curtisnewbie.module.ioc.util.LogUtil.info;

/**
 * @author yongjie.zhuang
 */
public abstract class AbstractBeanRegistry extends DefaultSingletonBeanRegistry
        implements LogMutable, InjectCapableBeanRegistry, RefreshableRegistry {

    private static final Logger logger = LogUtil.getLogger(DefaultSingletonBeanRegistry.class);
    private final AtomicBoolean isLogMuted = new AtomicBoolean(false);


    /** Indicate whether this registry is initialized, registry can only be initialised for once */
    private boolean isInitialised = false;

    /**
     * Dependencies map; bean name to its dependencies
     * <p>
     * Dependencies will be the name of the required bean. E.g,. if bean A depends on bean B, but the B is an interface,
     * and its actual implementation is type C, then the dependent bean name will still be B.
     */
    private final Map<String, Set<String>> dependenciesMap = new ConcurrentHashMap<>();

    /** List of BeanPostProcessors that process the bean after instantiation */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public AbstractBeanRegistry(BeanNameGenerator beanNameGenerator) {
        super(beanNameGenerator);
    }

    @Override
    public void refresh() {

        synchronized (getMutex()) {

            // can only be initialised once
            if (isInitialised)
                throw new ContextInitializedException("Bean registry cannot be initialized multiple times");
            isInitialised = true;

            logIfNotMuted("Starts loading bean registry");

            // prepare the registry before starting to instantiate beans and inject dependencies
            prepareBeanRegistry();
            logIfNotMuted("Bean registry prepared");

            // load bean definitions
            loadBeanDefinitions();
            logIfNotMuted("Bean definitions loaded");

            // pre-instantiate all the singleton beans first
            for (String beanName : beanDefinitionMap.keySet()) {
                getBeanByName(beanName);
            }
            logIfNotMuted("Bean registry refreshed");
        }
    }

    @Override
    public Object getBeanByName(String aliasOrName) {
        Object bean = super.getBeanByName(aliasOrName);
        if (bean != null)
            return bean;
        return createBean(aliasOrName);
    }

    private Object createBean(String aliasOrName) {
        final String beanName = getBeanName(aliasOrName);
        Objects.requireNonNull(beanName);

        final BeanDefinition beanDefinition = getBeanDefinition(beanName);
        // beanDefinition is not found, the bean is not registered at all
        if (beanDefinition == null) {
            throw BeanNotFoundException.forBeanName(beanName);
        }
        // instantiate bean
        Object bean = instantiateBean(beanDefinition);
        // after instantiation
        boolean shouldInitialize = applyPostProcessAfterInstantiation(bean, beanName);
        if (shouldInitialize) {
            // initialize bean
            bean = initializeBean(bean, beanName);
        }
        return bean;
    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        // before instantiation
        Object bean = applyPostProcessBeforeInstantiation(beanDefinition);
        if (bean == null) {
            // instantiate the bean
            bean = doCreateBean(beanDefinition);
            Objects.requireNonNull(bean, "Bean is not instantiated");
            registerSingletonBean(beanDefinition.getName(), bean);
        }
        return bean;
    }

    private Object initializeBean(Object bean, String beanName) {
        bean = applyPostProcessBeforeInitialization(beanName, bean);

        // here we will do some initialization, not supported just yet

        bean = applyPostProcessAfterInitialization(beanName, bean);
        return bean;
    }

    private Object applyPostProcessAfterInitialization(String beanName, Object bean) {
        for (BeanPostProcessor bpp : beanPostProcessors) {
            Object btu = bpp.postProcessAfterInitialization(beanName, bean);
            if (btu == null)
                return bean;
            bean = btu;
        }
        return bean;
    }

    private Object applyPostProcessBeforeInitialization(String beanName, Object bean) {
        for (BeanPostProcessor bpp : beanPostProcessors) {
            Object btu = bpp.postProcessBeforeInitialization(beanName, bean);
            if (btu == null)
                return bean;
            bean = btu;
        }
        return bean;
    }

    private boolean applyPostProcessAfterInstantiation(Object bean, String beanName) {
        for (BeanPostProcessor bpp : beanPostProcessors) {
            if (bpp instanceof InstantiationAwareBeanPostProcessor) {
                boolean shouldInitialize =
                        ((InstantiationAwareBeanPostProcessor) bpp).postProcessBeanAfterInstantiation(bean, beanName);
                if (!shouldInitialize)
                    return false;
            }
        }
        return true;
    }

    private Object applyPostProcessBeforeInstantiation(BeanDefinition beanDefinition) {
        for (BeanPostProcessor bpp : beanPostProcessors) {
            if (bpp instanceof InstantiationAwareBeanPostProcessor) {
                Object btu =
                        ((InstantiationAwareBeanPostProcessor) bpp).postProcessBeanBeforeInstantiation(beanDefinition);
                if (btu != null)
                    return btu;
            }
        }
        return null;
    }

    /**
     * Load {@code BeanDefinition}
     */
    protected abstract void loadBeanDefinitions();

    /**
     * Create bean instance
     *
     * @param beanDefinition beanDefinition
     * @return bean instance
     */
    protected abstract Object doCreateBean(BeanDefinition beanDefinition);

    @Override
    public void muteLog() {
        isLogMuted.set(true);
    }

    @Override
    public boolean isLogMuted() {
        return isLogMuted.get();
    }

    @Override
    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Objects.requireNonNull(beanPostProcessor);
        synchronized (getMutex()) {
            this.beanPostProcessors.add(beanPostProcessor);
        }
    }

    /** Prepare the bean registry before starting any actual bean scanning, bean instantiation, and dependency injection */
    protected void prepareBeanRegistry() {
        /*
        register itself as resolved dependency that can be later than be injected into other beans,
        but only the BeanRegistry interface can be used for dependency injection
         */
        this.registerSingletonBean(BeanRegistry.class, this);
    }

    @Override
    public boolean canMuteLog() {
        return true;
    }

    private void logIfNotMuted(String formatStr, Object... args) {
        if (!isLogMuted())
            info(logger, formatStr, args);
    }

    @Override
    public void registerDependency(String beanName, String dependentBeanName) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(dependentBeanName);

        synchronized (this.dependenciesMap) {
            this.dependenciesMap.computeIfAbsent(beanName, k -> new HashSet<>());
            this.dependenciesMap.get(beanName).add(dependentBeanName);
        }
    }

    @Override
    public boolean isDependent(String beanName, String dependentBeanName) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(dependentBeanName);

        synchronized (this.dependenciesMap) {
            return this.checkIsDependentRecursively(beanName, dependentBeanName, new HashSet<>());
        }
    }

    /**
     * Recursively check if the other bean is a dependent bean
     *
     * @param beanName          current bean
     * @param dependentBeanName the other bean that current bean might be dependent on
     * @param beansSeen         the beans that we have seen
     */
    private boolean checkIsDependentRecursively(String beanName, String dependentBeanName, Set<String> beansSeen) {
        // circular dependency, not dependent
        if (beansSeen.contains(beanName)) {
            return false;
        }
        // not found, even in a recursive way
        Set<String> dependentBeans = this.dependenciesMap.get(beanName);
        if (dependentBeans == null || dependentBeans.isEmpty())
            return false;
        if (dependentBeans.contains(dependentBeanName)) {
            return true;
        }
        // check transitive dependencies
        for (String d : dependentBeans) {
            beansSeen.add(d);
            if (checkIsDependentRecursively(d, dependentBeanName, beansSeen)) {
                return true;
            }
        }
        return false;
    }
}
