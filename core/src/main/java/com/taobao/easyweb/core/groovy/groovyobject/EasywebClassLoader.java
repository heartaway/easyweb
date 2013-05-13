package com.taobao.easyweb.core.groovy.groovyobject;

import groovy.lang.GroovyClassLoader;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 ����11:31
 * <p/>
 * �̳�GroovyClassLoader����Ҫ����web��������ʾGroovyObject�����classloader���ͣ���������webҳ��ı仯
 */
public class EasywebClassLoader extends GroovyClassLoader {

    private boolean web = false;

    public EasywebClassLoader(boolean web) {
        super();
        this.web = web;
    }

    public EasywebClassLoader(boolean web, ClassLoader parent) {
        super(parent);
        this.web = web;
    }

    public boolean isWeb() {
        return web;
    }
}
