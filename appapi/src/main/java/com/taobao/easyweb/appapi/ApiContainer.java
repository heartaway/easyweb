package com.taobao.easyweb.appapi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.change.AppChangeAdapter;

/**
 * apiÈÝÆ÷
 *
 * @author jimmey
 */
public class ApiContainer extends AppChangeAdapter {

    public static Map<String, Map<String, ApiMethod>> appApis = new ConcurrentHashMap<String, Map<String, ApiMethod>>();

    public static void regist(App app, ApiMethod method) {
        Map<String, ApiMethod> apis = getAppApis(app.getAppKey());
        apis.put(method.getName(), method);
    }

    public static ApiMethod getApi(String appKey, String name) {
        return getAppApis(appKey).get(name);
    }

    public static Map<String, ApiMethod> getAppApis(String appKey) {
        Map<String, ApiMethod> apis = appApis.get(appKey);
        if (apis == null) {
            apis = new HashMap<String, ApiMethod>();
            appApis.put(appKey, apis);
        }
        return apis;
    }

    @Override
    public void stop(App app) {
        remove(app);
    }

    @Override
    public void remove(App app) {
        appApis.remove(app.getAppKey());
    }

    @Override
    public void deployError(App app) {
        remove(app);
    }
}
