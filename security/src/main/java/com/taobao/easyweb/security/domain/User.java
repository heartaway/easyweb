package com.taobao.easyweb.security.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {

    /**
     * �û�id
     */
    private Integer id;

    /**
     * �û�����
     */
    private String name;

    /**
     * �û����룬����û��
     */
    private String password;

    private Date gmtCreate;

    private Date gmtModified;


    private Set<String> permissions = new HashSet<String>();

    private List<Role> roles = new ArrayList<Role>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<String> getRoleNames() {
        List<String> l = new ArrayList<String>();
        for (Role r : roles) {
            l.add(r.getName());
        }
        return l;
    }
}
