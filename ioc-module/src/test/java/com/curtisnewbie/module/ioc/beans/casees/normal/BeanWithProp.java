package com.curtisnewbie.module.ioc.beans.casees.normal;

import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.annotations.PropertyValue;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithProp implements KnowWhoIAm {

    @PropertyValue("test.bean-name")
    private String beanName;

    @PropertyValue("test.bean-created-by")
    private String createdBy;

    @PropertyValue("test.bean-version")
    private Integer beanVersion;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getBeanVersion() {
        return beanVersion;
    }

    public void setBeanVersion(Integer beanVersion) {
        this.beanVersion = beanVersion;
    }

    @Override
    public String toString() {
        return "BeanWithProp{" +
                "beanName='" + beanName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", beanVersion=" + beanVersion +
                '}';
    }
}
