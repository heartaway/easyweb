package com.taobao.easyweb.console.instance.domain;

import java.util.Date;

/**
 * easywebʵ������Ӧһ̨Ӧ�û�����ÿ��ʵ���Ͽ��Բ���ͬ��app
 * 
 * @author jimmey
 * 
 */
public class Instance {
	/**
	 * ʵ��id
	 */
	private Integer id;
	/**
	 * ��������
	 */
	private Integer groupId;
	/**
	 * ʵ��ip
	 */
	private String ip;
	/**
	 * ʵ������
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
