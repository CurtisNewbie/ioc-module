package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.beans.casees.circular.*;
import com.curtisnewbie.module.ioc.beans.casees.invalid.BeanWithNotAccessibleField;
import com.curtisnewbie.module.ioc.beans.casees.invalid.BeanWithNotSupportedProperties;
import com.curtisnewbie.module.ioc.beans.casees.invalid.EmptyBean;
import com.curtisnewbie.module.ioc.beans.casees.invalid.InterfaceWithMBean;
import com.curtisnewbie.module.ioc.beans.casees.normal.*;
import com.curtisnewbie.module.ioc.exceptions.CircularDependencyException;
import com.curtisnewbie.module.ioc.exceptions.TypeNotSupportedForInjectionException;
import com.curtisnewbie.module.ioc.exceptions.UnableToInjectDependencyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * @author yongjie.zhuang
 */
@TestInstance(value = TestInstance.Lifecycle.PER_METHOD)
public class BeanRegistryTest {

    private static final Logger logger = Logger.getLogger(BeanRegistryTest.class.toString());

    @Test
    public void shouldDetectNotAccessibleProperty() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer, BeanWithNotAccessibleField.class, EmptyBean.class);

        Assertions.assertThrows(UnableToInjectDependencyException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not accessible field, might have a bug");

        logger.info("Test: bean with not accessible field -- passed");
    }

    @Test
    public void shouldNotAllowInterfaceWithMBeanAnnotation() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer, InterfaceWithMBean.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should throw exception for interface with @MBean, might have a bug");

        logger.info("Test: @Bean on interfaces -- passed");
    }

    @Test
    public void shouldDetectNotInjectableBasicTypes() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer, BeanWithNotSupportedProperties.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable basic types (e.g., primitive types), might have a bug");

        logger.info("Test: bean with not injectable types -- passed");
    }

    @Test
    public void shouldDetectDirectCircularDependencies() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer,
                DirectCircularDependencyServiceAImpl.class,
                DirectCircularDependencyServiceBImpl.class);

        Assertions.assertThrows(CircularDependencyException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect direct circular dependency, might have a bug");

        logger.info("Test: beans with direct circular dependencies -- passed");
    }

    @Test
    public void shouldDetectIndirectCircularDependencies() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer,
                IndirectCircularDependencyServiceAImpl.class,
                IndirectCircularDependencyServiceBImpl.class,
                IndirectCircularDependencyServiceCImpl.class);

        Assertions.assertThrows(CircularDependencyException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect indirect/transitive circular dependency, might have a bug");

        logger.info("Test: beans with transitive/indirect circular dependencies -- passed");
    }

    @Test
    public void shouldInjectContextRelatedBeans() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer,
                BeanWithContextBeans.class);

        ApplicationContext applicationContext = contextInitializer.initialize(BeanRegistryTest.class);
        BeanRegistry registry = applicationContext.getBeanRegistry();

        BeanWithContextBeans beanWithContextBeans = registry.getBeanByClass(BeanWithContextBeans.class);
        Assertions.assertNotNull(beanWithContextBeans, "Bean not found after initialisation, might have a bug");
        Assertions.assertNotNull(beanWithContextBeans.getApplicationContext(), "Didn't inject ApplicationContext, might have a bug");
        Assertions.assertNotNull(beanWithContextBeans.getBeanRegistry(), "Didn't inject BeanRegistry, might have a bug");

        logger.info("Test: inject context-related beans (e.g., ApplicationContext) -- passed");
    }

    @Test
    public void shouldSuccessfullyInitialised() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer,
                AuthenticationManager.class,
                UserServiceImpl.class,
                ServiceAggregator.class);

        ApplicationContext applicationContext = contextInitializer.initialize(BeanRegistryTest.class);
        BeanRegistry registry = applicationContext.getBeanRegistry();

        // get bean by concrete class
        ServiceAggregator serviceAggregator = registry.getBeanByClass(ServiceAggregator.class);
        Assertions.assertNotNull(serviceAggregator, "Bean not found after initialisation, might have a bug");
//        serviceAggregator.whoIAm();

        // get bean by interface
        Service service = registry.getBeanByClass(Service.class);
        Assertions.assertNotNull(service, "Bean not found after initialisation, might have a bug");
//        service.whoIAm();

        logger.info("Test: normal dependency injection -- passed");
    }

    private void setupMockScanner(ContextInitializer mockedInitializer, Class<?>... clazzToBeFound) {
        DefaultSingletonBeanRegistry singletonBeanRegistry = DefaultSingletonBeanRegistry.class.cast(
                ApplicationContext.class.cast(mockedInitializer)
                        .getBeanRegistry()
        );
        MockBeanClassScanner mockBeanClassScanner = new MockBeanClassScanner();
        if (clazzToBeFound.length > 0)
            mockBeanClassScanner.setBeanClassesFound(new HashSet<>(Arrays.asList(clazzToBeFound)));
        singletonBeanRegistry.setBeanClassScanner(mockBeanClassScanner);
    }

}
