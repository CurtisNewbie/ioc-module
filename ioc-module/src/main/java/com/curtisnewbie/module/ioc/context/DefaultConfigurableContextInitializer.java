package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.processing.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
public class DefaultConfigurableContextInitializer implements ConfigurableContextInitializer {

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();
    private BeanNameGenerator beanNameGenerator;
    private BeanDependencyParser beanDependencyParser;
    private BeanClassScanner beanClassScanner;
    private BeanInstantiationStrategy beanInstantiationStrategy;
    private BeanAliasParser beanAliasParser;
    private PropertyRegistry propertyRegistry;

    private boolean isLogMuted = false;

    @Override
    public ApplicationContext initialize(Class<?> mainClazz) {
        // fallback to default implementation
        if (this.beanNameGenerator == null)
            this.beanNameGenerator = new BeanQualifiedNameGenerator();
        if (this.beanDependencyParser == null)
            this.beanDependencyParser = new AnnotatedBeanDependencyParser(beanNameGenerator);
        if (this.beanClassScanner == null)
            this.beanClassScanner = new AnnotatedBeanClassScanner();
        if (this.beanInstantiationStrategy == null)
            this.beanInstantiationStrategy = new DefaultConstructorInstantiationStrategy();
        if (this.beanAliasParser == null)
            this.beanAliasParser = new ParentClassBeanAliasParser(beanNameGenerator);
        if (this.propertyRegistry == null)
            this.propertyRegistry = new DefaultPropertyRegistry();

        DefaultApplicationContext ctx = new DefaultApplicationContext(
                this.beanDependencyParser,
                this.beanNameGenerator,
                this.beanClassScanner,
                this.beanInstantiationStrategy,
                this.beanAliasParser,
                this.beanPostProcessorList,
                this.propertyRegistry
        );
        // mute its log if necessary
        if (isLogMuted && ctx.canMuteLog())
            ctx.muteLog();
        // initialize context
        return ctx.initialize(mainClazz);
    }

    @Override
    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Objects.requireNonNull(beanPostProcessor);
        this.beanPostProcessorList.add(beanPostProcessor);
    }

    @Override
    public void registerBeanDependencyParser(BeanDependencyParser beanDependencyParser) {
        Objects.requireNonNull(beanDependencyParser);
        this.beanDependencyParser = beanDependencyParser;
    }

    @Override
    public void registerBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        Objects.requireNonNull(beanNameGenerator);
        this.beanNameGenerator = beanNameGenerator;
    }

    @Override
    public void registerBeanClassScanner(BeanClassScanner beanClassScanner) {
        Objects.requireNonNull(beanClassScanner);
        this.beanClassScanner = beanClassScanner;
    }

    @Override
    public void registerBeanInstantiationStrategy(BeanInstantiationStrategy beanInstantiationStrategy) {
        Objects.requireNonNull(beanInstantiationStrategy);
        this.beanInstantiationStrategy = beanInstantiationStrategy;
    }

    @Override
    public void registerBeanAliasParser(BeanAliasParser beanAliasParser) {
        Objects.requireNonNull(beanAliasParser);
        this.beanAliasParser = beanAliasParser;
    }

    @Override
    public void registerPropertyRegistry(PropertyRegistry propertyRegistry) {
        Objects.requireNonNull(propertyRegistry);
        this.propertyRegistry = propertyRegistry;
    }

    @Override
    public boolean canMuteLog() {
        return true;
    }

    @Override
    public void muteLog() {
        this.isLogMuted = true;
    }
}
