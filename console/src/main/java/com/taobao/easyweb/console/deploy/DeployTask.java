package com.taobao.easyweb.console.deploy;

import java.util.Date;

/**
 * ��������ÿ������ᴴ��һ�������߳�ȥ����
 * 
 * @author jimmey
 * 
 */
public class DeployTask {
	/**
	 * ����id
	 */
	private Integer id;
	/**
	 * ����Ļ���ip
	 */
	private String ip;
	/**
	 * ����ķ�����ip
	 */
	private String deployHost;
	/**
	 * �����Ӧ�ð汾id
	 */
	private String appName;
	
	private String appVersion;
	
	private String zipMd5;
	/**
	 * ������
	 */
	private String user;
	
	/**
	 * ����ʱ��
	 */
	private Date startTime;
	
	private String zipFile;
	/**
	 * ����״̬<br>
	 * 0���մ������ȴ�����<br>
	 * 1��������<br>
	 * 2������ɹ�<br>
	 * 3������ʧ��<br>
	 * 4��������<br>
	 * -1��ȡ������<br>
	 */
	private Integer status;
	/**
	 * ��������з��ص���־
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
