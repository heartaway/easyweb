package com.taobao.easyweb.console.app.domain;

import java.util.Date;

/**
 * Ӧ����Ϣ
 * 
 * @author jimmey
 * 
 */
public class AppInfo {

	private Integer id;

	private String appName;

	private String svnurl;

	private String adminName;
	
	private String description;
	/**
	 * Ӧ��״̬<br>
	 * 0:���룬δ��˴���<br>
	 * 1:���ͨ��������<br>
	 */
	private Integer status;

	private Date gmtCreate;

	private Date gmtModified;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSvnurl() {
		return svnurl;
	}

	public void setSvnurl(String svnurl) {
		this.svnurl = svnurl;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
