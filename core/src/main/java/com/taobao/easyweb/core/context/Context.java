package com.taobao.easyweb.core.context;

import com.taobao.easyweb.core.app.App;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Context {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private App app;
    /**
     * 运行时组件的相对路径，在groovy中直接渲染velocity的时候用到
     */
    private String currentPath;
    private String layout;
    private boolean securityCheck = true;
    private boolean easywebAdmin = false;
    private Integer statusCode;
    private String redirectTo;
    private boolean isDownload;
    private Logger appLogger;
    private Map<String, Object> contextMap = new HashMap<String, Object>();

    public Object getContext(String key) {
        return contextMap.get(key);
    }

    public String getAppName() {
        if (app == null) {
            return "easyweb";
        }
        return app.getAppKey();
    }

    public Map<String, Object> getContextMap() {
        return contextMap;
    }

    public void setContextMap(Map<String, Object> contextMap) {
        this.contextMap = contextMap;
    }

    public void putContext(String key, Object value) {
        this.contextMap.put(key, value);
    }

    public void putAll(Map<String, Object> contextMap) {
        this.contextMap.putAll(contextMap);
    }

    public boolean isSecurityCheck() {
        return securityCheck;
    }

    public void setSecurityCheck(boolean securityCheck) {
        this.securityCheck = securityCheck;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public boolean isEasywebAdmin() {
        return easywebAdmin;
    }

    public void setEasywebAdmin(boolean easywebAdmin) {
        this.easywebAdmin = easywebAdmin;
    }

    public Logger getAppLogger() {
        return appLogger;
    }

    public void setAppLogger(Logger appLogger) {
        this.appLogger = appLogger;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

}
