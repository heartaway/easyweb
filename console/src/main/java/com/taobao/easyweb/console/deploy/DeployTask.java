package com.taobao.easyweb.console.deploy;

import java.util.Date;

/**
 * 部署任务，每个任务会创建一个任务线程去处理
 * 
 * @author jimmey
 * 
 */
public class DeployTask {
	/**
	 * 任务id
	 */
	private Integer id;
	/**
	 * 部署的机器ip
	 */
	private String ip;
	/**
	 * 部署的服务器ip
	 */
	private String deployHost;
	/**
	 * 部署的应用版本id
	 */
	private String appName;
	
	private String appVersion;
	
	private String zipMd5;
	/**
	 * 部署人
	 */
	private String user;
	
	/**
	 * 部署时间
	 */
	private Date startTime;
	
	private String zipFile;
	/**
	 * 部署状态<br>
	 * 0：刚创建，等待部署<br>
	 * 1：部署中<br>
	 * 2：部署成功<br>
	 * 3：部署失败<br>
	 * 4：重试中<br>
	 * -1：取消部署<br>
	 */
	private Integer status;
	/**
	 * 部署过程中返回的日志
	 */
	private String deployLog;
	
	private Date gmtCreate;
	
	private Date gmtModified;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getZipMd5() {
		return zipMd5;
	}

	public void setZipMd5(String zipMd5) {
		this.zipMd5 = zipMd5;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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

	public String getDeployLog() {
		return deployLog;
	}

	public void setDeployLog(String deployLog) {
		this.deployLog = deployLog;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDeployHost() {
		return deployHost;
	}

	public void setDeployHost(String deployHost) {
		this.deployHost = deployHost;
	}

	public String getZipFile() {
		return zipFile;
	}

	public void setZipFile(String zipFile) {
		this.zipFile = zipFile;
	}
	
	
	
}
