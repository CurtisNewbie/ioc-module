package com.curtisnewbie.module.ioc.context;


import com.curtisnewbie.module.ioc.exceptions.ContextInitializedException;
import com.curtisnewbie.module.ioc.util.CountdownTimer;
import com.curtisnewbie.module.ioc.util.LogUtil;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Abstract application context
 *
 * @author yongjie.zhuang
 */
public abstract class AbstractApplicationContext implements ApplicationContext, ContextInitializer {

    private static final Logger logger = LogUtil.getLogger(AbstractApplicationContext.class);
    private volatile Class<?> mainClazz;
    private final AtomicBoolean isLogMuted = new AtomicBoolean(false);
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

        // start timer
        CountdownTimer timer = new CountdownTimer().start();

        // delegate to subclasses to initialise the actual context
        initializeContext();

        // stop timer
        timer.stop();

        if (!isLogMuted.get()) {
            LogUtil.info(
                    logger,
                    "ApplicationContext successfully initialized for %s, took %d milliseconds",
                    this.mainClazz.getName(),
                    timer.getMilliSeconds());
        }
        return this;
    }

    @Override
    public void muteLog() {
        isLogMuted.set(true);
    }

    @Override
    public boolean canMuteLog() {
        return true;
    }

    /**
     * Check if the log is supposed to be muted
     */
    protected boolean isLogMuted() {
        return isLogMuted.get();
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
