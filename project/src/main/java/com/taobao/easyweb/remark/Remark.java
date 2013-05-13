package com.taobao.easyweb.remark;

import java.util.Date;
import java.util.List;

/**
 * 
 * ��ע, ע��, ����, ����, ��ע, ����<br>
 * ���ĵ���FAQ�����ʡ����۵ȵȶ����������
 * 
 * @author jimmey
 * 
 */
public class Remark {

	private Integer id;

	/**
	 * ������id�������FAQ�������Ǵ�
	 */
	private Integer parentId;

	/**
	 * ����
	 */
	private String title;

	private String category;

	/**
	 * ��������
	 */
	private Integer type;

	/**
	 * 0:����<br>
	 * -1:ɾ��
	 */
	private Integer status;

	/**
	 * ������
	 */
	private String creator;
	
	private Integer creatorId;

	private String content;

	private Date gmtCreate;

	private Date gmtModified;
	/**
	 * �����ҵ����
	 */
	private Integer outerBiz;
	
	/**
	 * �ⲿ����ҵ��id
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
