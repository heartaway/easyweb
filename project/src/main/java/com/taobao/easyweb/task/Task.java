package com.taobao.easyweb.task;

import java.util.Date;
import java.util.List;

import com.taobao.easyweb.remark.Remark;
import com.taobao.easyweb.security.domain.User;

public class Task {

	private Integer id;

	/**
	 * 父任务id
	 */
	private Integer parentId;
	/**
	 * 任务标题
	 */
	private String title;
	/**
	 * 任务描述
	 */
	private String description;
	/**
	 * 任务发起人
	 */
	private Integer creator;
	
	private String creatorName;
	/**
	 * 指派人<br>
	 * 使用UserTask表管理，这里废弃
	 */
	private Integer assignTo;

	/**
	 * 任务截至时间
	 */
	private Date endTime;
	/**
	 * 
	 */
	private Integer status;
	/**
	 * 接入的业务类型
	 */
	private Integer outerBiz;
	/**
	 * 外部接入业务id
	 */
	private Integer outerBizId;

	private Date gmtCreate;

	private Date gmtModified;
	/**
	 * 任务评论等
	 */
	private List<Remark> comments;
	/**
	 * 被分配的人
	 */
	private List<User> users;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Integer getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Integer assignTo) {
		this.assignTo = assignTo;
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

	public Integer getOuterBiz() {
		return outerBiz;
	}

	public void setOuterBiz(Integer outerBiz) {
		this.outerBiz = outerBiz;
	}

	public Integer getOuterBizId() {
		return outerBizId;
	}

	public void setOuterBizId(Integer outerBizId) {
		this.outerBizId = outerBizId;
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

	public List<Remark> getComments() {
		return comments;
	}

	public void setComments(List<Remark> comments) {
		this.comments = comments;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

}
