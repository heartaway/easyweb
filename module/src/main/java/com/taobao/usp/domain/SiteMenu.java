package com.taobao.usp.domain;

import java.util.Date;
import java.util.List;

/**
 * User: jimmey
 * Date: 13-3-26
 * Time: 下午6:42
 *
 * 某个站点的建站后台菜单存储
 *
 */
public class SiteMenu {

    private Integer id;
    /**
     * 建站模型中的站点。
     */
    private Integer siteCategory;
//    /**
//     * 和Module中的含义一致
//     */
//    private Integer scope;

    /**
     * 这里的标题其实就是上面module的概念
     */
    private String title;

    private Integer menuItemId;

    private Integer appId;

    //冗余MenuItem中的parentId
    private Integer parentId;
    /**
     * 状态字段，1标示正常；0标示不正常，不显示
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
