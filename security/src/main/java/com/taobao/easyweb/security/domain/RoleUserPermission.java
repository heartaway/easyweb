package com.taobao.easyweb.security.domain;

import java.util.Date;

/**
 * 用户/角色 权限
 */
public class RoleUserPermission {

    /**
     * 数据库id
     */
    private Integer id;

    /**
     * 关联类型：1:user；2：role
     */
    private Integer type;// 1:user；2：role

    /**
     * 用户或角色id
     */
    private Integer roleOrUserId;

    /**
     * 权限id
     */
    private Integer permissionId;

    /**
     * 状态
     */
    private Integer status;

    private String reason;

    private Date gmtCreate;

    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRoleOrUserId() {
        return roleOrUserId;
    }

    public void setRoleOrUserId(Integer roleOrUserId) {
        this.roleOrUserId = roleOrUserId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
