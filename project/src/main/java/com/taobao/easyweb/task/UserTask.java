package com.taobao.easyweb.task;

import java.util.Date;

/**
 * �û������һ��������ܷ��������ˣ���ʱ��Ҫ��¼ÿ���˵�������Ϣ
 * 
 * @author jimmey
 * 
 */
public class UserTask {

	private Integer id;

	/**
	 * �û�id
	 */
	private Integer userId;
	/**
	 * ����id
	 */
	private Integer taskId;

	/**
	 * �������ʱ��
	 */
	private Date endTime;
	/**
	 * ����״̬��0�������У�1��������2������
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
