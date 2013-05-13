package com.taobao.easyweb.core.app;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.app.change.AppChangeHolder;
import com.taobao.easyweb.core.app.deploy.AppDeployer;
import com.taobao.easyweb.core.app.scanner.AppScanner;
import com.taobao.easyweb.core.app.scanner.ScanResult;
import com.taobao.easyweb.core.groovy.groovyobject.GroovyObjectLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午3:06
 */

@Component
public class AppMain implements InitializingBean {
    static Map<String, Long> modified = new ConcurrentHashMap<String, Long>();
    private static Map<String, Long> initializedApps = new ConcurrentHashMap<String, Long>();

    private Logger logger = AppLogger.getLogger();
    @Resource
    GroovyObjectLoader groovyObjectLoader;
    FileFilter folder = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && !pathname.getName().startsWith(".") && !pathname.getName().equals("target");
        }
    };
    Thread scanThread = new Thread(new Runnable() {
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }

            while (true) {
                try {
                    scan(System.currentTimeMillis());
                    Thread.sleep(3000);
                } catch (Throwable e) {
                    logger.error(e);
                }
            }
        }
    });
    @Resource
    private AppDeployer appDeployer;
    private String deployPath = Configuration.getDeployPath();
    private Map<String, App> appConfigs = new HashMap<String, App>();

    private static boolean isModified(File file) {
        String name = file.getAbsolutePath();
        long last = file.lastModified();
        Long l = modified.get(name);
        if (l == null || last != l.longValue()) {
            modified.put(name, last);
            return true;
        }
        return false;
    }

    public void scan(long startTime) throws Exception {
        File file = new File(deployPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] apps = file.listFiles(folder);
        for (File app : apps) {
            scanApp(app, startTime);
        }
    }

    private void scanApp(File appFile, long startTime) {
        // 解析app的根目录
        File config = new File(appFile.getPath() + "/app.properties");
        if (!config.exists()) {
            return;
        }
        App app = parseConfig(config);
        if (app == null || app.getName() == null) {
            return;
        }
        Logger appLogger = AppLogger.getAppLogger(app.getAppKey(), "deploy-app");
        ScanResult scanResult = AppScanner.getInstance().scan(app);

        if (!Configuration.isOnline() && !scanResult.getModifiedBizGroovy().isEmpty() && isInit(app)) {
            initializedApps.put(app.getAppKey(), 0L);
            try {
                appDeployer.deploy(app, scanResult);
            } catch (Exception e) {
                appLogger.error("deploy app error: " + app.getAppKey(), e);
                AppChangeHolder.deployError(app);
                return;
            }
            initializedApps.put(app.getAppKey(), System.currentTimeMillis());
        }

        if (!isInit(app)) {//没有部署或者被卸载了，则重新部署
            try {
                appDeployer.deploy(app, scanResult);
                initializedApps.put(app.getAppKey(), System.currentTimeMillis());
            } catch (Exception e) {
                appLogger.error("deploy app error: " + app.getAppKey(), e);
                initializedApps.put(app.getAppKey(), Long.MAX_VALUE);
                AppChangeHolder.deployError(app);
            }
        }
    }

    private boolean isInit(App app) {
        Long last = initializedApps.get(app.getAppKey());
        return last != null && last != 0 && last < Long.MAX_VALUE;
    }

    private App parseConfig(File file) {
        String name = file.getAbsolutePath();
        if (appConfigs.containsKey(name) && !isModified(file)) {
            return appConfigs.get(name);
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            App app = new App();
            app.setName(properties.getProperty(App.APP_NAME));
            app.setVersion(properties.getProperty(App.APP_VERSION, "1.0"));
            String webPathStr = properties.getProperty(App.APP_WEB_PATH, "");
            app.setWebPaths(Arrays.asList(webPathStr.split(",")));
            app.setRootPath(file.getParent());
            appConfigs.put(name, app);
            return app;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scanThread.setName("Easyweb Thread");
        scanThread.start();
    }
}
