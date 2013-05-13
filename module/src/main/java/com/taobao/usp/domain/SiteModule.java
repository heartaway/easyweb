package com.taobao.usp.domain;

import java.util.Date;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-2 ÏÂÎç9:47
 */
public class SiteModule {

    private Integer id;

    private Integer siteCategory;

    private Integer moduleId;

    private Date gmtCreate;

    private Date gmtModified;

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

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
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
}
