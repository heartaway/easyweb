package com.taobao.usp.domain;

import java.util.Date;
import java.util.List;

/**
 * User: jimmey
 * Date: 13-3-26
 * Time: ����6:42
 *
 * ĳ��վ��Ľ�վ��̨�˵��洢
 *
 */
public class SiteMenu {

    private Integer id;
    /**
     * ��վģ���е�վ�㡣
     */
    private Integer siteCategory;
//    /**
//     * ��Module�еĺ���һ��
//     */
//    private Integer scope;

    /**
     * ����ı�����ʵ��������module�ĸ���
     */
    private String title;

    private Integer menuItemId;

    private Integer appId;

    //����MenuItem�е�parentId
    private Integer parentId;
    /**
     * ״̬�ֶΣ�1��ʾ������0��ʾ������������ʾ
     */
    private Integer status;

    private Date gmtCreate;

    private Date gmtModified;

    private List<SiteMenu> children;

    private MenuItem menuItem;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SiteMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SiteMenu> children) {
        this.children = children;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
}
