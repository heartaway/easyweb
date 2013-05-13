package com.taobao.easyweb.core.request.debug;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.request.PageMethod;

import java.util.Map;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 ÏÂÎç11:55
 */
public class AppInfo {

    private App app;
    private Map<String, Object> beans;
    private Map<String, Object> vmTools;
    private Map<String, PageMethod> pages;

    public AppInfo(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Map<String, Object> getBeans() {
        return beans;
    }

    public void setBeans(Map<String, Object> beans) {
        this.beans = beans;
    }

    public Map<String, Object> getVmTools() {
        return vmTools;
    }

    public void setVmTools(Map<String, Object> vmTools) {
        this.vmTools = vmTools;
    }

    public Map<String, PageMethod> getPages() {
        return pages;
    }

    public void setPages(Map<String, PageMethod> pages) {
        this.pages = pages;
    }
}
