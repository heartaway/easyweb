package com.taobao.easyweb.core.groovy.groovyobject;

import groovy.lang.GroovyObject;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 ����12:10
 *
 *
 * groovy����Ļ�������װ
 */
public class CacheDO {
    /**
     * �����groovy����
     */
    private GroovyObject obj;
    /**
     * ��Ӧgroovy�ļ�������޸�ʱ��
     */
    private long lastModified;
    /**
     * groovy�����Ƿ��Ѿ�����������ע��
     */
    private boolean autowired;

    public GroovyObject getObj() {
        return obj;
    }

    public void setObj(GroovyObject obj) {
        this.obj = obj;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isAutowired() {
        return autowired;
    }

    public void setAutowired(boolean autowired) {
        this.autowired = autowired;
    }
}
