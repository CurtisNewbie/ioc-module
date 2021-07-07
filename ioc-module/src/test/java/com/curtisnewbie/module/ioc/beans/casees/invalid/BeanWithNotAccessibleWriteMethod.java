package com.curtisnewbie.module.ioc.beans.casees.invalid;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithNotAccessibleWriteMethod {

    @Dependency
    private EmptyBean dummyBean;

    public EmptyBean getDummyBean() {
        return dummyBean;
    }

    private void setDummyBean(EmptyBean dummyBean) {
        this.dummyBean = dummyBean;
    }
}
