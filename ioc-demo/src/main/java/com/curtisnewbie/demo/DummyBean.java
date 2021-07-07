package com.curtisnewbie.demo;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.annotations.PropertyValue;
import com.curtisnewbie.module.ioc.context.ApplicationContext;
import com.curtisnewbie.module.ioc.context.BeanRegistry;

/**
 * @author yongjie.zhuang
 */
@MBean
public class DummyBean implements HasName {

    @PropertyValue("dummy.name")
    private String dummyName;

    @PropertyValue("dummy.age")
    private int dummyAge;

    @Dependency
    private MaskedTommy maskedTommy;

    @Dependency
    private ApplicationContext applicationContext;

    @Dependency
    private BeanRegistry beanRegistry;

    public MaskedTommy getMaskedTommy() {
        return maskedTommy;
    }

    public void setMaskedTommy(MaskedTommy maskedTommy) {
        this.maskedTommy = maskedTommy;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public BeanRegistry getBeanRegistry() {
        return beanRegistry;
    }

    public void setBeanRegistry(BeanRegistry beanRegistry) {
        this.beanRegistry = beanRegistry;
    }

    public int getDummyAge() {
        return dummyAge;
    }

    public void setDummyAge(int dummyAge) {
        this.dummyAge = dummyAge;
    }

    public String getDummyName() {
        return dummyName;
    }

    public void setDummyName(String dummyName) {
        this.dummyName = dummyName;
    }

    @Override
    public String toString() {
        return "DummyBean{" +
                "dummyName='" + dummyName + '\'' +
                ", dummyAge=" + dummyAge +
                ", maskedTommy=" + maskedTommy +
                ", applicationContext=" + applicationContext +
                ", beanRegistry=" + beanRegistry +
                '}';
    }
}
