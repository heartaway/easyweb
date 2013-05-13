package com.taobao.usp.domain;


import java.util.Date;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 ����8:26
 *
 * ÿ��module��һ�������Ĺ���app��������easyweb app��Ҳ�������ⲿ��
 */
public class AppInfo {

    /**
     * module id
     */
    private Integer id;
    /**
     * ģ������÷�Χ</br>
     * 1����վС��</br>
     * 2��������ӪС��</br>
     * 3��վ��</br>
     * 4������</br>
     */
    private Integer scope;
    /**
     * ģ������
     */
    private String title;
    /**
     * ģ��������
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
