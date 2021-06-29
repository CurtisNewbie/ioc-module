package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.beans.casees.circular.*;
import com.curtisnewbie.module.ioc.beans.casees.invalid.BeanWithNotSupportedProperties;
import com.curtisnewbie.module.ioc.beans.casees.invalid.InterfaceWithMBean;
import com.curtisnewbie.module.ioc.beans.casees.normal.AuthenticationManager;
import com.curtisnewbie.module.ioc.beans.casees.normal.Service;
import com.curtisnewbie.module.ioc.beans.casees.normal.ServiceAggregator;
import com.curtisnewbie.module.ioc.beans.casees.normal.UserServiceImpl;
import com.curtisnewbie.module.ioc.exceptions.CircularDependencyException;
import com.curtisnewbie.module.ioc.exceptions.TypeNotSupportedForInjectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author yongjie.zhuang
 */
@TestInstance(value = TestInstance.Lifecycle.PER_METHOD)
public class BeanRegistryTest {

    @Test
    public void shouldNotAllowInterfaceWithMBeanAnnotation() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer, InterfaceWithMBean.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should throw exception for interface with @MBean, might have a bug");
    }

    @Test
    public void shouldDetectNotInjectableBasicTypes() {
        ContextInitializer contextInitializer = ContextFactory.getContextInitializer();
        setupMockScanner(contextInitializer, BeanWithNotSupportedProperties.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable basic types (e.g., primitive types), might have a bug");
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

        serviceAggregator.whoIAm();

        // get bean by interface
        Service service = registry.getBeanByClass(Service.class);
        Assertions.assertNotNull(serviceAggregator, "Bean not found after initialisation, might have a bug");

        service.whoIAm();
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
