package com.taobao.usp.domain;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jimmey
 * Date: 13-3-26
 * Time: ����6:44
 * To change this template use File | Settings | File Templates.
 */
public class MenuItem {

    /**
     * ���ݿ�id����ʵ��ҵ������
     */
    private Integer id;

    /**
     * ����ģ���id
     */
    private Integer moduleId;

    /**
     * �˵�����
     */
    private String title;

    /**
     * �����������Ͳ�����ֻ��uri
     */
    private String uri;

    /**
     * ������ʽ
     */
    private String param;

    private String groovyCode;

    /**
     * ���˵�id
     */
    private Integer parentId;

    /**
     * ��չ�ֶ�
     */
    private String feature;

    private String description;

    private Date gmtCreate;

    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getGroovyCode() {
        return groovyCode;
    }

    public void setGroovyCode(String groovyCode) {
        this.groovyCode = groovyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
