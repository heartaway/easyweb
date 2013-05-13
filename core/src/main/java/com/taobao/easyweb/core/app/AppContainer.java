package com.taobao.easyweb.core.app;

import com.taobao.easyweb.core.app.change.AppChangeAdapter;

import java.util.*;

public class AppContainer extends AppChangeAdapter {

    private static Map<String, App> deployedApps = new HashMap<String, App>();
    private static Map<String, Set<String>> appVersions = new HashMap<String, Set<String>>();
    private static Map<String, String> currentAppVersion = new HashMap<String, String>();

    public static void setCurrentVersion(String appName, String appVersion) {
        Set<String> versions = getAppVersions(appName);
        if (versions.contains(appVersion)) {
            currentAppVersion.put(appName, appVersion);
        }
    }

    public static Set<String> getAppVersions(String appName) {
        Set<String> versions = appVersions.get(appName);
        if (versions == null) {
            versions = new HashSet<String>();
        }
        return versions;
    }

    /**
     * 先用uri来控制app
     *
     * @param domain
     * @param uri
     * @return
     */
    public static App getAppInfo(String domain, String uri) {
        String[] v = uri.split("/");
        String app = v[1];
        if (app.equals("ewassets")) {// 公共assets服务
            app = "assets";
        }
        String version = currentAppVersion.get(app);
        if (version == null) {
            version = "1.0";
        }
        return deployedApps.get(app + "-" + version);
    }

    public static App getApp(String appName) {
        return getApp(appName, null);
    }

    public static App getApp(String appName, String version) {
        if (version != null) {
            App app = deployedApps.get(appName + "-" + version);
            if (app != null) {
                return app;
            }
        }
        if (version == null) {
            version = currentAppVersion.get(appName);
        }
        return deployedApps.get(appName + "-" + version);
    }

    public static String getCurrentVersion(String appName) {
        String version = currentAppVersion.get(appName);
        if (version == null) {
            version = "1.0";
        }
        return version;
    }

    /**
     * 简单注册app，但是没有其他任何信息
     *
     * @param appInfo
     */
    public static void regist(App appInfo) {
        appInfo.setStatus(0);
        deployedApps.put(appInfo.getAppKey(), appInfo);
    }

    public static Collection<App> getApps() {
        return deployedApps.values();
    }

    @Override
    public void remove(App app) {
        app.setStatus(-1);
        deployedApps.put(app.getAppKey(), app);
    }

    @Override
    public void stop(App app) {
        app.setStatus(0);
        deployedApps.put(app.getAppKey(), app);
    }

    @Override
    public void start(App appInfo) {
        Set<String> versions = getAppVersions(appInfo.getName());
        if (!versions.contains(appInfo.getVersion())) {
            versions.add(appInfo.getVersion());
        }
        if (!currentAppVersion.containsKey(appInfo.getName())) {
            currentAppVersion.put(appInfo.getName(), appInfo.getVersion());
        }
        appInfo.setStatus(1);
        deployedApps.put(appInfo.getAppKey(), appInfo);
    }

    public void deployError(App app) {
        app.setStatus(2);
        deployedApps.put(app.getAppKey(), app);
    }

}
