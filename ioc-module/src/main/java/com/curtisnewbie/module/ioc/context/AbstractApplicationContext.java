package com.curtisnewbie.module.ioc.context;


import com.curtisnewbie.module.ioc.exceptions.ContextInitializedException;

/**
 * Abstract application context
 *
 * @author yongjie.zhuang
 */
public abstract class AbstractApplicationContext implements ApplicationContext, ContextInitializer {

    private static volatile Class<?> mainClazz;

    /**
     * Initialise the application context
     *
     * @param mainClazz the class that contains the main(...) method
     */
    @Override
    public void initialize(Class<?> mainClazz) {
        synchronized (AbstractApplicationContext.class) {
            // validate if the application context has been initialised
            if (AbstractApplicationContext.mainClazz != null) {
                throw new ContextInitializedException("Context has been initialised, and it can only be initialised for once");
            }
            AbstractApplicationContext.mainClazz = mainClazz;
        }
        initializeContext();
    }

    /**
     * Get the main class of this application (or the given one that at least claimed to be the main class)
     *
     * @see #initialize(Class)
     */
    public Class<?> getMainClazz() {
        return AbstractApplicationContext.mainClazz;
    }

    /**
     * Initialize the context, which includes creating the beans and injecting the dependencies, this method is invoked
     * internally by the {@link AbstractApplicationContext}, and should not be used externally
     */
    protected abstract void initializeContext();
}
