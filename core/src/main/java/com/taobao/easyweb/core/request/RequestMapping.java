package com.taobao.easyweb.core.request;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.app.change.AppChangeAdapter;
import com.taobao.easyweb.core.context.ThreadContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMapping extends AppChangeAdapter {

    private static Map<String, Map<String, PageMethod>> appRequestMappings = new ConcurrentHashMap<String, Map<String, PageMethod>>();

    /**
     * @param request
     * @return
     */
    public static PageMethod getPageMthod(HttpServletRequest request) {
        Map<String, PageMethod> appMappings = getAppPages();
        if (appMappings != null) {
            String url = getUrl(request);
            return appMappings.get(url);
        }
        return null;
    }

    /**
     * °üº¬ request methodºÍuri
     *
     * @param request
     * @return
     */
    public static String getUrl(HttpServletRequest request) {
        return request.getMethod().toLowerCase() + ":" + request.getRequestURI();
    }

    public static Map<String, PageMethod> getAppPages(String appKey) {
        Map<String, PageMethod> appMappings = appRequestMappings.get(appKey);
        if (appMappings != null) {
            return appMappings;
        }
        return Collections.emptyMap();
    }

    public static Map<String, PageMethod> getAppPages() {
        return getAppPages(ThreadContext.getContext().getApp().getAppKey());
    }

    public static void addPageMethod(String appKey, PageMethod pageMethod) {
        Map<String, PageMethod> appMappings = appRequestMappings.get(appKey);
        if (appMappings == null) {
            appMappings = new HashMap<String, PageMethod>();
            appRequestMappings.put(appKey, appMappings);
        }
        AppLogger.getAppLogger(appKey, "RegistPage").warn("regist " + pageMethod.getUrl());
        appMappings.put(pageMethod.getUrl(), pageMethod);
    }

    @Override
    public void remove(App app) {
        appRequestMappings.remove(app.getAppKey());
    }

    @Override
    public void deployError(App app) {
        remove(app);
    }
}
