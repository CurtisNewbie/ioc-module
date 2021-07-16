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
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author yongjie.zhuang
 */
@TestInstance(value = TestInstance.Lifecycle.PER_METHOD)
public class BeanRegistryTest {

    private static final Logger logger = Logger.getLogger(BeanRegistryTest.class.toString());

    @Test
    public void shouldDetectNotAccessibleWriteMethod() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithNotAccessibleWriteMethod.class, EmptyBean.class);

        Assertions.assertThrows(UnableToInjectDependencyException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not accessible field, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotAccessibleProperty() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithNotAccessibleField.class, EmptyBean.class);

        Assertions.assertThrows(UnableToInjectDependencyException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not accessible field, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldNotAllowInterfaceWithMBeanAnnotation() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, InterfaceWithMBean.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should throw exception for interface with @MBean, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableIntType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithInt.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableIntegerType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedInteger.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableStringType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithString.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedDoubleType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedDouble.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableDoubleType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithDouble.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedCharType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedChar.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableCharType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithChar.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedLongType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedLong.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableLongType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithLong.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedFloatType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedFloat.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableFloatType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithFloat.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedShortType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedShort.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableShortType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithShort.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableBoxedByteType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithBoxedByte.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectNotInjectableByteType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithByte.class);

        Assertions.assertThrows(TypeNotSupportedForInjectionException.class, () -> {
            contextInitializer.initialize(BeanRegistryTest.class);
        }, "Should detect not injectable type, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldDetectDirectCircularDependencies() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
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
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
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
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
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
    public void shouldSetContextRelatedBeansThroughAwareInterfaces() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer,
                ContextAwareBean.class);

        ApplicationContext applicationContext = contextInitializer.initialize(BeanRegistryTest.class);
        BeanRegistry registry = applicationContext.getBeanRegistry();

        ContextAwareBean contextAwareBean = registry.getBeanByClass(ContextAwareBean.class);
        Assertions.assertNotNull(contextAwareBean, "Bean not found after initialisation, might have a bug");
        Assertions.assertNotNull(contextAwareBean.getApplicationContext(), "Didn't inject ApplicationContext, might have a bug");
        Assertions.assertNotNull(contextAwareBean.getBeanRegistry(), "Didn't inject BeanRegistry, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldInjectPropertyValues() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer, BeanWithProp.class);

        ApplicationContext applicationContext = contextInitializer.initialize(BeanRegistryTest.class);
        BeanRegistry registry = applicationContext.getBeanRegistry();

        // get bean by concrete class
        BeanWithProp beanWithProp = registry.getBeanByClass(BeanWithProp.class);
        Assertions.assertNotNull(beanWithProp, "Bean not found after initialisation, might have a bug");
        Assertions.assertNotNull(beanWithProp.getBeanName(), "Bean property not injected, might have a bug");
        Assertions.assertNotNull(beanWithProp.getBeanVersion(), "Bean property not injected, might have a bug");
        Assertions.assertNotNull(beanWithProp.getCreatedBy(), "Bean property not injected, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldSuccessfullyInitialised() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
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
        Assertions.assertNotNull(serviceAggregator.getAbstractManager(), "Didn't inject dependent, might have a bug");
        Assertions.assertNotNull(serviceAggregator.getService(), "Didn't inject dependent, might have a bug");
        Assertions.assertNotNull(serviceAggregator.getUserServiceImpl(), "Didn't inject dependent, might have a bug");

        // get bean by interface
        Service service = registry.getBeanByClass(Service.class);
        Assertions.assertNotNull(service, "Bean not found after initialisation, might have a bug");

        logger.info("Test passed");
    }

    @Test
    public void shouldBeansOfType() {
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        setupMockScanner(contextInitializer,
                AuthenticationManager.class,
                UserServiceImpl.class,
                ServiceAggregator.class);

        ApplicationContext applicationContext = contextInitializer.initialize(BeanRegistryTest.class);
        BeanRegistry registry = applicationContext.getBeanRegistry();

        // get map of beans by a parent type
        Map<String, Object> beanMap = registry.getBeansOfType(KnowWhoIAm.class);
        Assertions.assertNotNull(beanMap, "#getBeansOfType should not return null");
        Assertions.assertFalse(beanMap.isEmpty());
        for (Map.Entry<String, Object> bean : beanMap.entrySet()) {
            Assertions.assertNotNull(bean.getValue(), "Bean instance of " + bean.getKey() + " should not be null");
        }
        logger.info("Test passed");
    }

    private void setupMockScanner(ContextInitializer mockedInitializer, Class<?>... clazzToBeFound) {
        ConfigurableContextInitializer ctx = (ConfigurableContextInitializer) mockedInitializer;
        if (ctx.canMuteLog())
            ctx.muteLog();

        MockBeanClassScanner mockBeanClassScanner = new MockBeanClassScanner();
        if (clazzToBeFound.length > 0)
            mockBeanClassScanner.setBeanClassesFound(new HashSet<>(Arrays.asList(clazzToBeFound)));

        // set the mocked beanClassScanner
        ctx.registerBeanClassScanner(mockBeanClassScanner);
    }

}
