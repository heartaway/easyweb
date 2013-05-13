package com.taobao.easyweb.console.instance.domain;

import java.util.Date;

/**
 * easyweb实例，对应一台应用机器，每个实例上可以部署不同的app
 * 
 * @author jimmey
 * 
 */
public class Instance {
	/**
	 * 实例id
	 */
	private Integer id;
	/**
	 * 所属分组
	 */
	private Integer groupId;
	/**
	 * 实例ip
	 */
	private String ip;
	/**
	 * 实例名称
	 */
	private String name;
	
	private Date lastCheck;
	
	private Integer status;
	
	private Date gmtCreate;
	
	private Date gmtModified;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getLastCheck() {
		return lastCheck;
	}
	public void setLastCheck(Date lastCheck) {
		this.lastCheck = lastCheck;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
