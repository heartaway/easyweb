package com.taobao.easyweb.core.velocity;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.change.AppChangeAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 ÏÂÎç6:31
 */
public class VmToolFactory extends AppChangeAdapter {

    private static Map<String, Map<String, Object>> appVmTools = new HashMap<String, Map<String, Object>>();

    public static void putAppTool(String appKey, String name, Object object) {
        getAppTools(appKey).put(name, object);
    }

    public static Map<String, Object> getAppTools(String appKey) {
        Map<String, Object> map = appVmTools.get(appKey);
        if (map == null) {
            map = new HashMap<String, Object>();
            appVmTools.put(appKey, map);
        }
        return map;
    }

    public void stop(App app) {
        appVmTools.remove(app.getAppKey());
    }

    public void remove(App app) {
        appVmTools.remove(app.getAppKey());
    }

    public void deployError(App app) {
        remove(app);
    }


}
