package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.exceptions.TypeNotSupportedForInjectionException;
import com.curtisnewbie.module.ioc.processing.*;

import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;

/**
 * <p>
 * Extension of {@link DefaultSingletonBeanRegistry}, provide functionalities to load {@code BeanDefinition} and
 * instantiate beans
 * </p>
 *
 * @author yongjie.zhuang
 */
public class DefaultInjectCapableBeanRegistry extends AbstractBeanRegistry {

    private final BeanClassScanner beanClzScanner;
    private final BeanAliasParser beanAliasParser;
    private final BeanInstantiationStrategy beanInstantiationStrategy;

    public DefaultInjectCapableBeanRegistry(BeanClassScanner beanClassScanner,
                                            BeanNameGenerator beanNameGenerator,
                                            BeanInstantiationStrategy beanInstantiationStrategy,
                                            BeanAliasParser beanAliasParser) {
        super(beanNameGenerator);
        this.beanClzScanner = beanClassScanner;
        this.beanAliasParser = beanAliasParser;
        this.beanInstantiationStrategy = beanInstantiationStrategy;
    }

    @Override
    protected Object doCreateBean(BeanDefinition beanDefinition) {
        Objects.requireNonNull(beanDefinition);

        // use BeanInstantiationStrategy to instantiate the bean, the dependencies are not resolved yet
        return beanInstantiationStrategy.instantiateBean(beanDefinition);
    }

    @Override
    protected void prepareBeanRegistry() {
        /*
        register itself as resolved dependency that can be later than be injected into other beans,
        but only the BeanRegistry interface can be used for dependency injection
         */
        this.registerSingletonBean(BeanRegistry.class, this);
        this.registerSingletonBean(InjectCapableBeanRegistry.class, this);
    }

    @Override
    protected void loadBeanDefinitions() {
        // set of classes of beans that will be managed by this context
        Set<Class<?>> managedBeanClasses = beanClzScanner.scanBeanClasses();

        // register these managed beans, including their interfaces as aliases
        registerBeanDefinitions(managedBeanClasses);
    }


    /**
     * Register a set of beans' classes that are found by the scanner
     * <br>
     * Registering, means loading these classes and their generated names in forms of {@link BeanDefinition}, this
     * method doesn't involve populating the beans and injecting their dependencies.
     * <br>
     * This method also collect their interfaces, and use them as aliases, such that we can find a bean (of the actual
     * concrete implementation) by one of the interface that it implements.
     */
    private void registerBeanDefinitions(Set<Class<?>> managedBeanClasses) {
        for (Class<?> c : managedBeanClasses) {
            if (c.isInterface()) {
                throw new TypeNotSupportedForInjectionException(
                        format("Interface cannot be created as a managed bean (e.g., using %s), type: %s",
                                MBean.class.getSimpleName(), c.toString())
                );
            }
            String beanName = generateBeanName(c);

            // register bean definition
            registerBeanDefinition(beanName, new DefaultBeanDefinition(c, beanName));

            // register the superClass and interfaces' name as this bean's alias
            Set<String> aliases = beanAliasParser.parseBeanAliases(c);

            /*
            if the map already contains this alias,
            this means that two or more beans are sharing the same interface, but
            it doesn't necessary cause circular dependency, as long as the different interfaces/superclasses
            are used for injection
             */
            for (String al : aliases) {
                addAlias(beanName, al);
            }
        }
    }

}
