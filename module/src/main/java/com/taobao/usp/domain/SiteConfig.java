package com.taobao.usp.domain;

import java.util.Date;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-3 上午12:16
 */
public class SiteConfig {

    /**
     * 数据库主键
     */
    private Integer id;
    /**
     * 站点分类id
     */
    private Integer siteCategory;
    /**
     * 所属功能模块id
     */
    private Integer moduleId;
    /**
     * 配置key
     */
    private String key;
    /**
     * 配置值，初始情况下就是元配置的默认值
     */
    private String value;
    private Date gmtCreate;
    private Date gmtModified;

    private ConfigInfo configInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiteCategory() {
        return siteCategory;
    }

    public void setSiteCategory(Integer siteCategory) {
        this.siteCategory = siteCategory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public ConfigInfo getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(ConfigInfo configInfo) {
        this.configInfo = configInfo;
    }
}
