package com.taobao.easyweb.core.request.pipeline;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.change.AppChangeAdapter;
import com.taobao.easyweb.core.context.Context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 ÉÏÎç11:53
 */
public class Pipeline extends AppChangeAdapter {

    private static Map<String, List<Valve>> appValves = new ConcurrentHashMap<String, List<Valve>>();

    public static void initPipeline(App app, List<Valve> valves) {
        appValves.put(app.getAppKey(), valves);
    }

    public static void invoke(Context context) throws Exception {
        List<Valve> valves = appValves.get(context.getApp().getAppKey());
        if (valves == null || valves.isEmpty()) {
            return;
        }
        for (Valve valve : valves) {
            valve.invoke(context);
        }
    }

    @Override
    public void remove(App app) {
        appValves.remove(app.getAppKey());
    }

    @Override
    public void stop(App app) {
        remove(app);
    }

}
