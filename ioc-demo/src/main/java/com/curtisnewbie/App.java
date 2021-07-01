package com.curtisnewbie;

import com.curtisnewbie.demo.DummyBean;
import com.curtisnewbie.demo.MaskedTommy;
import com.curtisnewbie.demo.SomebodyBean;
import com.curtisnewbie.demo.TommyBean;
import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.context.ApplicationContextAware;
import com.curtisnewbie.module.ioc.context.BeanRegistry;
import com.curtisnewbie.module.ioc.context.ContextFactory;

@MBean
public class App implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {

        // initialise context
        ContextFactory.getNewContextInitializer().initialize(App.class);

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
