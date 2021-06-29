package com.curtisnewbie.module.ioc.context;


import com.curtisnewbie.module.ioc.exceptions.ContextInitializedException;

/**
 * Abstract application context
 *
 * @author yongjie.zhuang
 */
public abstract class AbstractApplicationContext implements ApplicationContext, ContextInitializer {

    private volatile Class<?> mainClazz;
    private final Object mutex = new Object();

    @Override
    public ApplicationContext initialize(Class<?> mainClazz) {
        synchronized (mutex) {
            // validate if the application context has been initialised
            if (this.mainClazz != null) {
                throw new ContextInitializedException("Context has been initialised, and it can only be initialised for once");
            }
            this.mainClazz = mainClazz;
        }
        initializeContext();
        return this;
    }

    /**
     * Get the main class of this application (or the given one that at least claimed to be the main class)
     *
     * @see #initialize(Class)
     */
    public Class<?> getMainClazz() {
        return this.mainClazz;
    }

    /**
     * Initialize the context, which includes creating the beans and injecting the dependencies, this method is invoked
     * internally by the {@link AbstractApplicationContext}, and should not be used externally
     */
    protected abstract void initializeContext();
}
