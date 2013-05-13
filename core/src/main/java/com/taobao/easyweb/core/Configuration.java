package com.taobao.easyweb.core;

import com.taobao.easyweb.core.app.App;

import java.io.StringWriter;
import java.net.InetAddress;
import java.util.Properties;

public class Configuration {

    //    public static String APP_DEPLOY_TEMP_PATH = System.getProperty("user.home")+"/easyweb/deploy/temp/";
    public static String PATH = "file.resource.loader.path";
    public static String CACHE = "file.resource.loader.cache";
    public static String CHECK = "file.resource.loader.modificationCheckInterval";
    // ���벿���Ŀ¼
    public static String APP_DEPLOY_PATH = "app.deploy.path";
    // ���ش��뿪����·�������ڱ��ؿ�������ʹ��
    public static String APP_DEV_PATH = "app.dev.path";
    //easyweb��־Ŀ¼
    public static String LOG_ROOT = "log.root";
    /**
     * ����ģʽ��ֵ��
     * 1.dev
     * 2.daily
     * 3.online
     */
    public static String envMode = "envMode";
    public static String staticFilePath = "staticFilePath";
    public static String staticFileCacheTime = "staticFileCacheTime";
    public static String httpCharset = "httpCharset";
    public static String httpSuffix = "httpSuffix";
    public static String dynamicHsf = "dynamicHsf";
    public static String securityCehck = "securityCehck";
    public static String deployPath, devPath;
    private static Properties properties = new Properties();
    private static String ip = "127.0.0.1";

    static {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
        }
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("easyweb_default.properties"));
            // �ⲿ���õ�
            Properties config = new Properties();
            config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("easyweb.properties"));
            set(config, PATH);
            set(config, CACHE);
            set(config, CHECK);
            set(config, APP_DEPLOY_PATH);
            set(config, APP_DEV_PATH);
            set(config, envMode);
            set(config, staticFilePath);
            set(config, staticFileCacheTime);
            set(config, httpCharset);
            set(config, dynamicHsf);
            set(config, securityCehck);
            set(config, LOG_ROOT);
        } catch (Exception e) {
        }
    }

    private static void set(Properties config, String name) {
        if (config.getProperty(name) != null) {
            properties.setProperty(name, config.getProperty(name));
        }
    }

    public static String getIp() {
        return ip;
    }

    public static String getAppLog(String appKey) {
        return getLogRoot() + appKey + ".log";
    }

    private static String getLogRoot() {
        String root = properties.getProperty(LOG_ROOT, System.getProperty("user.home") + "/easyweb/logs/");
        if (!root.endsWith("/")) {
            root += "/";
        }
        return root;
    }

    public static String getEnvMode() {
        String mode = properties.getProperty(envMode, "online");
        if (!mode.equals("dev") && !mode.equals("daily") && !mode.equals("online")) {
            mode = "online";
        }
        return mode;
    }

    /**
     * ��������
     *
     * @return
     */
    public static boolean isDev() {
        return "dev".equals(getEnvMode());
    }

    /**
     * ���Ի���
     *
     * @return
     */
    public static boolean isDaily() {
        return "daily".equals(getEnvMode());
    }

    /**
     * ���ɻ��������ϡ�Ԥ��
     *
     * @return
     */
    public static boolean isOnline() {
        return "online".equals(getEnvMode());
    }

    public static String getDeployPath() {
        if (isDev()) {
            return getDevPath();
        }
        if (deployPath == null) {
            deployPath = getValue(APP_DEPLOY_PATH, System.getProperty("user.home") + "/easyweb/apps");
            if (!deployPath.endsWith("/")) {
                deployPath = deployPath + "/";
            }
        }
        return deployPath;
    }

    public static String getClasspath(App app) {
        return getClasspathRoot() + app.getAppKey();
    }

    private static String getClasspathRoot() {
        return getValue(APP_DEPLOY_PATH, System.getProperty("user.home") + "/easyweb/apps") + "_classes/";
    }

    public static String getDeployTempPath() {
        return getDeployPath() + "_temp";
    }

    /**
     * ����·��
     *
     * @return
     */
    public static String getDevPath() {
        if (devPath == null) {
            devPath = properties.getProperty(APP_DEV_PATH, System.getProperty("user.home") + "/easyweb/editor/");
            if (!devPath.endsWith("/")) {
                devPath = devPath + "/";
            }
        }
        return devPath;
    }

    private static String getValue(String key) {
        return properties.getProperty(key);
    }

    private static String getValue(String key, String defVal) {
        return properties.getProperty(key, defVal);
    }

    private static int getInt(String key) {
        String v = getValue(key);
        try {
            return Integer.valueOf(v);
        } catch (Exception e) {
            return 0;
        }
    }

    private static int getInt(String key, int defVal) {
        int v = getInt(key);
        return v > 0 ? v : defVal;
    }

//    public static String getVelocityCache() {
//        return properties.getProperty(CACHE, "true");
//    }
//
//    public static String getVelocityCheck() {
//        return properties.getProperty(CHECK, "3");
//    }

//    public static String getStaticFilePath() {
//        String v = properties.getProperty(staticFilePath);
//        if (v.endsWith("/")) {
//            v += "/";
//        }
//        return v;
//    }

    public static int getStaticCacheTime() {
        if ("dev".equals(getEnvMode())) {
            return 1;
        }

        return getInt(staticFileCacheTime, 30);
    }

    public static String getHttpCharset() {
        return getValue(httpCharset, "GBK");
    }

    public static String getHttpSuffix() {
        return getValue(httpSuffix, "html");
    }

    public static String print() {
        StringWriter sb = new StringWriter();
        sb.append("��־��Ŀ¼��").append(getLogRoot()).append("\n");
        sb.append("��ǰ����ģʽ��").append(getEnvMode()).append("\n");
        sb.append("Դ���벿��Ŀ¼��").append(getDeployPath()).append("\n");
        sb.append("Class����Ŀ¼��").append(getClasspathRoot()).append("\n");
        sb.append("���ؿ���Ŀ¼��").append(getDevPath()).append("\n");
        sb.append("��̬�ļ�����ʱ�䣺").append(getStaticCacheTime()+"").append("\n");
        sb.append("HTTP���룺").append(getHttpCharset()).append("\n");
        sb.append("\n=============Runtime Configuration start============\n");
        try {
            properties.store(sb,"runtime configuration");
        }catch (Exception e){
        }
        return sb.toString();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
