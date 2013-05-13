package com.taobao.easyweb.core.app;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "app")
public class App {

    public static String APP_NAME = "app.name";
    public static String APP_VERSION = "app.version";
    public static String APP_WEB_PATH = "app.web.path";
    /**
     * app����
     */
    private String name;
    /**
     * app�汾
     */
    private String version;
    private List<String> webPaths;
    /**
     * app���������Բ���
     */
    private String domain;
    /**
     * Ӧ��װ�ޣ�
     * 0��������
     * 1������
     * 2������ʧ��
     * -1��ɾ��
     */
    private int status = 0;
    /**
     * app��svn��ַ
     */
    private String svnUrl;
    private Map<String, String> dependencies = new HashMap<String, String>();
    private Set<String> dailyIps = new HashSet<String>();
    private Set<String> onlineIps = new HashSet<String>();
    private String rootPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    // public String getPackageRootPath() {
    // return packageRootPath;
    // }
    //
    // public void setPackageRootPath(String packageRootPath) {
    // this.packageRootPath = packageRootPath;
    // }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }

    public Set<String> getDailyIps() {
        return dailyIps;
    }

    public void setDailyIps(Set<String> dailyIps) {
        this.dailyIps = dailyIps;
    }

    public Set<String> getOnlineIps() {
        return onlineIps;
    }

    public void setOnlineIps(Set<String> onlineIps) {
        this.onlineIps = onlineIps;
    }

    public String getAppKey() {
        return name + "-" + version;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public List<String> getWebPaths() {
        return webPaths;
    }

    public void setWebPaths(List<String> webPaths) {
        this.webPaths = webPaths;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
