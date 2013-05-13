package com.taobao.easyweb.core.groovy.groovyobject;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.change.AppChangeAdapter;
import com.taobao.easyweb.core.app.change.ListenerPriority;
import com.taobao.easyweb.core.app.change.Priority;
import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 ����12:12
 * <p/>
 * CacheDO�Ļ�������������classloader���л��洦��
 */
@ListenerPriority(Priority.HIGH)
public class Cacher extends AppChangeAdapter {

    private static Map<GroovyClassLoader, Map<String, CacheDO>> cacheDOMap = new ConcurrentHashMap<GroovyClassLoader, Map<String, CacheDO>>();

    public static void put(GroovyClassLoader classLoader, File file, CacheDO cacheDO) {
        getClassloaderCache(classLoader).put(file.getAbsolutePath(), cacheDO);
    }

    public static CacheDO get(GroovyClassLoader classLoader, File file) {
        CacheDO cacheDO = getClassloaderCache(classLoader).get(file.getAbsolutePath());
        if (cacheDO == null) {
            return null;
        }
        return (cacheDO.getLastModified() == file.lastModified()) ? cacheDO : null;
    }

    private static Map<String, CacheDO> getClassloaderCache(GroovyClassLoader classLoader) {
        Map<String, CacheDO> map = cacheDOMap.get(classLoader);
        if (map == null) {
            map = new ConcurrentHashMap<String, CacheDO>();
            cacheDOMap.put(classLoader, map);
        }
        return map;
    }

    public static void clean(GroovyClassLoader classLoader) {
        cacheDOMap.remove(classLoader);
    }

    @Override
    public void init() {
        //
    }

    @Override
    public void stop(App app) {
        GroovyClassLoader classLoader =  AppClassLoaderFactory.getAppBizClassLoader(app);
        cacheDOMap.remove(classLoader);
    }

    @Override
    public void start(App app) {

    }
}
