package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.beans.BeanPropertyInfo;
import com.curtisnewbie.module.ioc.beans.DependentBeanInfo;
import com.curtisnewbie.module.ioc.context.SingletonBeanRegistry;
import com.curtisnewbie.module.ioc.exceptions.CircularDependencyException;
import com.curtisnewbie.module.ioc.exceptions.UnsatisfiedDependencyException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;

/**
 * Post processor for injecting dependencies
 *
 * @author yongjie.zhuang
 */
public class DependencyInjectionBeanPostProcessor implements InstantiationAwareBeanPostProcessor{

    private SingletonBeanRegistry singletonBeanRegistry;
    private BeanDependencyParser beanDependencyParser;
    private Set<String> beanResolved = new HashSet<>();

    public DependencyInjectionBeanPostProcessor(SingletonBeanRegistry singletonBeanRegistry,
                                                BeanDependencyParser beanDependencyParser) {
        this.singletonBeanRegistry = singletonBeanRegistry;
        this.beanDependencyParser = beanDependencyParser;
    }

    @Override
    public Object postProcessBeanAfterInstantiation(Object bean, String beanName) {
        resolveDependenciesRecursively(bean, beanName);
        return bean;
    }


    /**
     * Resolve dependencies between beans (in beanNameSet) recursively
     * <p>
     * The child nodes are resolved first, then the parent nodes. Eventually, there will be a child node that doesn't
     * have any dependency, so we can just simply create it, and inject it into its 'parent' bean, this is essentially
     * how it works.
     * </p>
     * <p>
     * The dependencies between beans are just like trees (or, strictly speaking, a graph). A graph without circles
     * (circular dependencies) is essentially a n-node tree.
     * </p>
     */
    private void resolveDependenciesRecursively(Object bean, String beanName) {
        Objects.requireNonNull(beanName);

        // bean has been resolved
        if (beanResolved.contains(singletonBeanRegistry.getBeanName(beanName)))
            return;

        Class<?> beanClz = bean.getClass();

        /*
        get list of dependent beans of this bean, and the properties each dependent will be injected into,
        E.g., if current bean is Bean A, Bean A requires Bean B at field fieldOne.
         Then the Bean B and field fieldOne together will become a single DependentBeanInfo
         */
        List<DependentBeanInfo> dependentBeans = beanDependencyParser.parseDependencies(beanClz);
        for (DependentBeanInfo dependent : dependentBeans) {
            String dependentAlias = dependent.getDependentBeanName();

            // detect unresolvable dependency
            if (!singletonBeanRegistry.containsBean(dependentAlias)) {
                throw new UnsatisfiedDependencyException("Detected unresolvable dependency: " + dependentAlias);
            }
            // detect circular dependency
            if (singletonBeanRegistry.isDependent(beanName, dependentAlias)) {
                throw new CircularDependencyException("Detected circular dependency between " + beanName + " and " + dependentAlias);
            }
            // update dependency cache
            singletonBeanRegistry.registerDependency(beanName, dependentAlias);

            // try to get the instantiated dependent bean, see if it's actually populated
            Object dependentBean = singletonBeanRegistry.getBeanByName(dependentAlias);
            Objects.requireNonNull(dependentBean, format("Dependent Bean: '%s' has not yet been instantiated, unable to inject dependencies",
                    dependentAlias));

            // continue to resolve the dependent bean
            resolveDependenciesRecursively(dependentBean, dependentAlias);
        }

        // inject dependencies
        for (DependentBeanInfo dependent : dependentBeans) {
            injectDependentBean(bean, dependent);
        }

        // mark the bean as resolved
        beanResolved.add(singletonBeanRegistry.getBeanName(beanName));
    }


    /**
     * Inject a dependent bean into the target bean
     *
     * @param bean              bean
     * @param dependentBeanInfo info of a dependent bean
     */
    private void injectDependentBean(Object bean, DependentBeanInfo dependentBeanInfo) {
        Objects.requireNonNull(bean, "Unable to inject dependencies, bean is null");
        Objects.requireNonNull(dependentBeanInfo, "Unable to inject dependencies, dependent bean info is null");

        // dependent bean's name (which might be an alias)
        String dependentBeanName = dependentBeanInfo.getDependentBeanName();

        // the properties info of this bean that require dependency injection
        List<BeanPropertyInfo> toBeInjectedProperties = dependentBeanInfo.getBeanPropertiesToInject();

        Objects.requireNonNull(dependentBeanName, "Unable to inject dependencies, dependent bean's name is null");
        Objects.requireNonNull(toBeInjectedProperties, "Unable to inject dependencies, properties list to be injected is null");

        if (toBeInjectedProperties == null || toBeInjectedProperties.isEmpty())
            return;

        // the actual implementation bean, the required type might be an interface, so we need to handle the casting
        Object dependentImplBeanInstance = singletonBeanRegistry.getBeanByName(dependentBeanName);
        Objects.requireNonNull(dependentImplBeanInstance, "Unable to find instance of bean: " + dependentBeanName);

        for (BeanPropertyInfo prop : toBeInjectedProperties) {
            if (prop.canSatisfyRequiredType(dependentImplBeanInstance.getClass())) {
                // inject the dependent bean into the field
                prop.setValueToPropertyOfBean(bean, dependentImplBeanInstance);
            }
        }
    }
}
