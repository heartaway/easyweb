package com.taobao.easyweb.model;

import java.util.Date;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-15 ÏÂÎç2:40
 */
public class ModelBackup {

    private Integer id;

    private Integer moduelId;

    private String data;

    private Date gmtCreate;

    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuelId() {
        return moduelId;
    }

    public void setModuelId(Integer moduelId) {
        this.moduelId = moduelId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
