package com.curtisnewbie.module.ioc.context;

import java.util.Map;

/**
 * Abstract application context
 *
 * @author yongjie.zhuang
 */
public abstract class AbstractApplicationContext implements ContextAware, ContextInitializer {

    private static volatile Class<?> mainClazz;

    /**
     * Initialise the application context
     *
     * @param mainClazz the class that contains the main(...) method
     */
    public void initialize(Class<?> mainClazz) {
        // validate if the application context has been initialised
        if (AbstractApplicationContext.mainClazz != null) {
            throw new IllegalStateException("Context has been initialised, and it can only be initialised for once");
        }
        synchronized (AbstractApplicationContext.class) {
            AbstractApplicationContext.mainClazz = mainClazz;
        }
        initializeContext();
    }

    /**
     * Get the main class of this application (or the given one that at least claimed to be the main class)
     *
     * @see #initialize(Class)
     */
    protected Class<?> getMainClazz() {
        return AbstractApplicationContext.mainClazz;
    }

    /**
     * Get the classLoader of the main class
     */
    protected ClassLoader getClassLoader() {
        return getMainClazz().getClassLoader();
    }

    /**
     * Initialize the context, which includes creating the beans and injecting the dependencies, this method is invoked
     * internally by the {@link AbstractApplicationContext}, and should not be used externally
     */
    protected abstract void initializeContext();
}
