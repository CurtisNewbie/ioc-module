package com.curtisnewbie.module.ioc.demo;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class SomebodyBean {

    @Dependency
    private DummyBean dummyBean;

    @Dependency
    private TommyBean tommyBean;

    public DummyBean getDummyBean() {
        return dummyBean;
    }

    public void setDummyBean(DummyBean dummyBean) {
        this.dummyBean = dummyBean;
    }

    public TommyBean getTommyBean() {
        return tommyBean;
    }

    public void setTommyBean(TommyBean tommyBean) {
        this.tommyBean = tommyBean;
    }

    @Override
    public String toString() {
        return "SomebodyBean{" +
                "dummyBean=" + dummyBean +
                ", tommyBean=" + tommyBean +
                '}';
    }
}
