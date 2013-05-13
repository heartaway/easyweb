package com.taobao.easyweb.task;

import java.util.Date;

/**
 * 用户任务表。一个任务可能分配给多个人，这时需要记录每个人的任务信息
 * 
 * @author jimmey
 * 
 */
public class UserTask {

	private Integer id;

	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 任务id
	 */
	private Integer taskId;

	/**
	 * 任务结束时间
	 */
	private Date endTime;
	/**
	 * 任务状态：0：进行中；1：结束；2：延期
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
