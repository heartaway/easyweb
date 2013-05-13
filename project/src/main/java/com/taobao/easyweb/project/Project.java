package com.taobao.easyweb.project;

import java.util.Date;

public class Project {

	/**
	 * 项目id
	 */
	private Integer id;
	/**
	 * 父项目id
	 */
	private Integer parentId;
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 创建人
	 */
	private Integer creator;
	/**
	 * 项目描述
	 */
	private String description;
	
	private String thumbnails;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 0:公开<br>
	 * 1:私有<br>
	 */
	private Integer shareType = 0;

	private Date endTime;

	private Date gmtCreate;

	private Date gmtModified;

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

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(String thumbnails) {
		this.thumbnails = thumbnails;
	}
	
}
