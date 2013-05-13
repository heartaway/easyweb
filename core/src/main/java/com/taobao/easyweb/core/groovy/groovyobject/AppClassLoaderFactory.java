package com.taobao.easyweb.core.groovy.groovyobject;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.change.AppChangeAdapter;
import com.taobao.easyweb.core.app.change.ListenerPriority;
import com.taobao.easyweb.core.app.change.Priority;
import groovy.lang.GroovyClassLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 ����12:03
 */
@ListenerPriority(Priority.LOW)
public class AppClassLoaderFactory extends AppChangeAdapter {

    private static Map<String, AppGroovyClassLoader> appGroovyClassLoaderMap = new ConcurrentHashMap<String, AppGroovyClassLoader>();

    public static AppGroovyClassLoader getAppClassLoader(App app) {
        AppGroovyClassLoader classLoader = appGroovyClassLoaderMap.get(app.getAppKey());
        if (classLoader == null) {
            classLoader = new AppGroovyClassLoader(app);
            appGroovyClassLoaderMap.put(app.getAppKey(), classLoader);
        }
        return classLoader;
    }

    /**
     * ����app��classloader�����ڿ������ճ�ģʽ
     *
     * @param app
     */
    public static void reset(App app) {
        AppGroovyClassLoader classLoader = appGroovyClassLoaderMap.get(app.getAppKey());
        classLoader = null;//�Ƿ���Ҫ��ʾ
        AppGroovyClassLoader newloader = new AppGroovyClassLoader(app);
        appGroovyClassLoaderMap.put(app.getAppKey(), newloader);
    }

    public static GroovyClassLoader getAppBizClassLoader(App app) {
        return getAppClassLoader(app).getBizClassLoader();
    }

    public static GroovyClassLoader getAppWebClassLoader(App app) {
        return getAppClassLoader(app).getWebClassLoader();
    }

    /**
     * Ӧ��ֹͣҲ����classloader
     *
     * @param app
     */
    @Override
    public void stop(App app) {
        remove(app);
    }

    /**
     * ж��һ��Ӧ�ú����Ե�groovy class ж��
     *
     * @param ap
     */
    @Override
    public void remove(App ap) {
        appGroovyClassLoaderMap.remove(ap.getAppKey());
    }
}
