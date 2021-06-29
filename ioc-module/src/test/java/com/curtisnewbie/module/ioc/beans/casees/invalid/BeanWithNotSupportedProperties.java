package com.curtisnewbie.module.ioc.beans.casees.invalid;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class BeanWithNotSupportedProperties {

    @Dependency
    private Integer boxedIntField;

    @Dependency
    private int intField;

    @Dependency
    private String stringField;

    @Dependency
    private long longField;

    @Dependency
    private Long boxedLongField;

    @Dependency
    private double doubleField;

    @Dependency
    private Double boxedDoubleField;

    @Dependency
    private short shortField;

    @Dependency
    private Short boxedShortField;

    @Dependency
    private byte byteField;

    @Dependency
    private Byte boxedByteField;

    public Integer getBoxedIntField() {
        return boxedIntField;
    }

    public void setBoxedIntField(Integer boxedIntField) {
        this.boxedIntField = boxedIntField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public long getLongField() {
        return longField;
    }

    public void setLongField(long longField) {
        this.longField = longField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(double doubleField) {
        this.doubleField = doubleField;
    }

    public short getShortField() {
        return shortField;
    }

    public void setShortField(short shortField) {
        this.shortField = shortField;
    }

    public Long getBoxedLongField() {
        return boxedLongField;
    }

    public void setBoxedLongField(Long boxedLongField) {
        this.boxedLongField = boxedLongField;
    }

    public Double getBoxedDoubleField() {
        return boxedDoubleField;
    }

    public void setBoxedDoubleField(Double boxedDoubleField) {
        this.boxedDoubleField = boxedDoubleField;
    }

    public Short getBoxedShortField() {
        return boxedShortField;
    }

    public void setBoxedShortField(Short boxedShortField) {
        this.boxedShortField = boxedShortField;
    }

    public byte getByteField() {
        return byteField;
    }

    public void setByteField(byte byteField) {
        this.byteField = byteField;
    }

    public Byte getBoxedByteField() {
        return boxedByteField;
    }

    public void setBoxedByteField(Byte boxedByteField) {
        this.boxedByteField = boxedByteField;
    }
}

