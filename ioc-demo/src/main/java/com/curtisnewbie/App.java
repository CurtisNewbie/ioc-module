package com.curtisnewbie;

import com.curtisnewbie.demo.DummyBean;
import com.curtisnewbie.demo.MaskedTommy;
import com.curtisnewbie.demo.SomebodyBean;
import com.curtisnewbie.demo.TommyBean;
import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.aware.ApplicationContextAware;
import com.curtisnewbie.module.ioc.context.BeanRegistry;
import com.curtisnewbie.module.ioc.context.ApplicationContextFactory;
import com.curtisnewbie.module.ioc.context.ContextInitializer;

@MBean
public class App implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {

        // initialise context
        ContextInitializer contextInitializer = ApplicationContextFactory.getNewContextInitializer();
        // might mute the logs in context and context initializer
//        if (contextInitializer.canMuteLog())
//            contextInitializer.muteLog();
        contextInitializer.initialize(App.class);

        // get registry
        BeanRegistry registry = applicationContext.getBeanRegistry();

        // demo
        SomebodyBean b = registry.getBeanByClass(SomebodyBean.class);
        b.sayName();

        MaskedTommy mt = registry.getBeanByClass(MaskedTommy.class);
        mt.sayName();

        TommyBean tb = registry.getBeanByClass(TommyBean.class);
        tb.sayName();

        DummyBean db = registry.getBeanByClass(DummyBean.class);
        db.sayName();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        App.applicationContext = applicationContext;
    }
}
