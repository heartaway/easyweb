package com.taobao.easyweb.console.app.domain;

import java.util.Date;

public class AppVersion {

	private Integer id;
	
	private Integer appName;
	
	private String version;
	
	private String description;
	
	private String svnurl;
	
	private Date gmtCreate;
	
	private Date gmtModified;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAppName() {
		return appName;
	}

	public void setAppName(Integer appName) {
		this.appName = appName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSvnurl() {
		return svnurl;
	}

	public void setSvnurl(String svnurl) {
		this.svnurl = svnurl;
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
