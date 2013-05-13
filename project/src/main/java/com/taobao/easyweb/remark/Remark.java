package com.taobao.easyweb.remark;

import java.util.Date;
import java.util.List;

/**
 * 
 * 备注, 注释, 发言, 评语, 评注, 备考<br>
 * 如文档、FAQ、提问、评论等等都在这个类中
 * 
 * @author jimmey
 * 
 */
public class Remark {

	private Integer id;

	/**
	 * 关联的id，如果是FAQ类型则是答
	 */
	private Integer parentId;

	/**
	 * 标题
	 */
	private String title;

	private String category;

	/**
	 * 内容类型
	 */
	private Integer type;

	/**
	 * 0:正常<br>
	 * -1:删除
	 */
	private Integer status;

	/**
	 * 发布者
	 */
	private String creator;
	
	private Integer creatorId;

	private String content;

	private Date gmtCreate;

	private Date gmtModified;
	/**
	 * 接入的业务编号
	 */
	private Integer outerBiz;
	
	/**
	 * 外部关联业务id
	 */
	private Integer outerBizId;
	

	private List<Remark> children;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Remark> getChildren() {
		return children;
	}

	public void setChildren(List<Remark> children) {
		this.children = children;
	}

	public Integer getOuterBizId() {
		return outerBizId;
	}

	public void setOuterBizId(Integer outerBizId) {
		this.outerBizId = outerBizId;
	}

	public Integer getOuterBiz() {
		return outerBiz;
	}

	public void setOuterBiz(Integer outerBiz) {
		this.outerBiz = outerBiz;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

}
