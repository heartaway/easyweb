package com.taobao.easyweb.security.domain;

import java.util.Date;

public class Permission {

    /**
     * ���ݿ�id
     */
    private Integer id;

    /**
     * Ȩ�������ռ�
     */
    private String namespace;

    /**
     * Ȩ��Ψһkey
     */
    private String key;
    /**
     * Ȩ����������
     */
    private String description;
    /**
     * ����ʱ��
     */
    private Date gmtCreate;
    /**
     * ����޸�ʱ��
     */
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
