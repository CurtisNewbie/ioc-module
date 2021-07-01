package com.curtisnewbie.demo;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class SomebodyBean implements HasName {

    @Dependency
    private DummyBean dummyBean;

    @Dependency
    private TommyBean tommyBean;

    @Dependency
    private FurtherMaskedTommy furtherMaskedTommy;

//    @Dependency
//    private int notInjectableInt;

//    @Dependency
//    private String notInjectableString;

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

    public FurtherMaskedTommy getFurtherMaskedTommy() {
        return furtherMaskedTommy;
    }

    public void setFurtherMaskedTommy(FurtherMaskedTommy furtherMaskedTommy) {
        this.furtherMaskedTommy = furtherMaskedTommy;
    }

//    public String getNotInjectableString() {
//        return notInjectableString;
//    }
//
//    public void setNotInjectableString(String notInjectableString) {
//        this.notInjectableString = notInjectableString;
//    }

//    public int getNotInjectableInt() {
//        return notInjectableInt;
//    }
//
//    public void setNotInjectableInt(int notInjectableInt) {
//        this.notInjectableInt = notInjectableInt;
//    }


    @Override
    public String toString() {
        return "SomebodyBean{" +
                "dummyBean=" + dummyBean +
                ", tommyBean=" + tommyBean +
                ", furtherMaskedTommy=" + furtherMaskedTommy +
                '}';
    }
}
