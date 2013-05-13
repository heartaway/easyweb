package com.taobao.easyweb.core.groovy.groovyobject;

import com.taobao.easyweb.core.app.App;
import groovy.lang.GroovyClassLoader;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 ����7:40
 */
public class AppGroovyClassLoader {

    private App app;
    private EasywebClassLoader bizClassLoader;
    /**
     * web�����classloader��biz����loader������biz�����ڳ�ʼ����ɺ�Ϳ��Ը�webʹ����
     */
    private EasywebClassLoader webClassLoader;

    public AppGroovyClassLoader(App app) {
        this.app = app;
        this.bizClassLoader = new EasywebClassLoader(false);
        this.webClassLoader = new EasywebClassLoader(true, this.bizClassLoader);
    }

    public GroovyClassLoader getBizClassLoader() {
        return this.bizClassLoader;
    }

    public GroovyClassLoader getWebClassLoader() {
        return this.webClassLoader;
    }
}
