package com.taobao.usp.domain;


import java.util.Date;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 下午8:26
 *
 * 每个module是一个独立的功能app，可以是easyweb app，也可以是外部的
 */
public class AppInfo {

    /**
     * module id
     */
    private Integer id;
    /**
     * 模块的适用范围</br>
     * 1、建站小二</br>
     * 2、物种运营小二</br>
     * 3、站长</br>
     * 4、其它</br>
     */
    private Integer scope;
    /**
     * 模块名称
     */
    private String title;
    /**
     * 模块所有者
     */
    private String owner;

    private String description;
    private Date gmtCreate;
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
