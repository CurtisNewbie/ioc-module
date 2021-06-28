package com.curtisnewbie;

import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.context.ApplicationContextAware;
import com.curtisnewbie.module.ioc.context.ContextFactory;
import com.curtisnewbie.module.ioc.demo.SomebodyBean;

@MBean
public class App implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {

        ContextFactory.getContextInitializer().initialize(App.class);

        SomebodyBean b = applicationContext.getBeanRegistry().getBeanByClass(SomebodyBean.class);

        System.out.println(b.toString());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        App.applicationContext = applicationContext;
    }
}
