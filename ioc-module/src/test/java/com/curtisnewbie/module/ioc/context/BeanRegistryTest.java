package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.beans.casees.circular.*;
import com.curtisnewbie.module.ioc.beans.casees.invalid.*;
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
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithNotAccessibleField.class, EmptyBean.class);

        Assertions.assertThrows(UnableToInjectDependencyException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not accessible field, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldNotAllowInterfaceWithMBeanAnnotation() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, InterfaceWithMBean.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should throw exception for interface with @MBean, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableIntType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithInt.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableIntegerType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedInteger.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableStringType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithString.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedDoubleType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedDouble.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableDoubleType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithDouble.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedLongType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedLong.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableLongType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithLong.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedFloatType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedFloat.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableFloatType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithFloat.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedShortType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedShort.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableShortType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithShort.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedByteType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedByte.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableByteType() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithByte.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectDirectCircularDependencies() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer,
                DirectCircularDependencyServiceAImpl.class,
                DirectCircularDependencyServiceBImpl.class);

        Assertions.assertThrows(CircularDependencyException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect direct circular dependency, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectIndirectCircularDependencies() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer,
                IndirectCircularDependencyServiceAImpl.class,
                IndirectCircularDependencyServiceBImpl.class,
                IndirectCircularDependencyServiceCImpl.class);

        Assertions.assertThrows(CircularDependencyException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect indirect/transitive circular dependency, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldInjectContextRelatedBeans() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer,
                BeanWithContextBeans.class);

        ApplicationContext applicationContext = contextInitializer.initialize(BeanRegistryTest.class);
        BeanRegistry registry = applicationContext.getBeanRegistry();

        BeanWithContextBeans beanWithContextBeans = registry.getBeanByClass(BeanWithContextBeans.class);
        Assertions.assertNotNull(beanWithContextBeans, "Bean not found after initialisation, might have a bug");
        Assertions.assertNotNull(beanWithContextBeans.getApplicationContext(), "Didn't inject ApplicationContext, might have a bug");
        Assertions.assertNotNull(beanWithContextBeans.getBeanRegistry(), "Didn't inject BeanRegistry, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldSuccessfullyInitialised() {
        ContextInitializer contextInitializer = ContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer,
                AuthenticationManager.class,
                UserServiceImpl.class,
                ServiceAggregator.class);

        ApplicationContext applicationContext = contextInitializer.initialize(BeanRegistryTest.class);
        BeanRegistry registry = applicationContext.getBeanRegistry();

        // get bean by concrete class
        ServiceAggregator serviceAggregator = registry.getBeanByClass(ServiceAggregator.class);
        Assertions.assertNotNull(serviceAggregator, "Bean not found after initialisation, might have a bug");
        Assertions.assertNotNull(serviceAggregator.getAuthenticationManager(), "Didn't inject dependent, might have a bug");
        Assertions.assertNotNull(serviceAggregator.getService(), "Didn't inject dependent, might have a bug");
        Assertions.assertNotNull(serviceAggregator.getUserServiceImpl(), "Didn't inject dependent, might have a bug");

        // get bean by interface
        Service service = registry.getBeanByClass(Service.class);
        Assertions.assertNotNull(service, "Bean not found after initialisation, might have a bug");

    }

    private void setupMockScanner(ContextInitializer mockedInitializer, Class<?>... clazzToBeFound) {
        if (mockedInitializer.canMuteLog())
            mockedInitializer.muteLog();
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
