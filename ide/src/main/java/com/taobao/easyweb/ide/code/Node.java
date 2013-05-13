package com.taobao.easyweb.ide.code;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;


public class Node {
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 节点名称,按照不同风格来展现
	 */
	private String nodeName;

	private String appName;

	private String nodeId;

	private String text;

	private String id;

	private String iconCls;

	private String parentNode;
	/**
	 * 包名，创建文件的时候使用
	 */
	private String packageName;

	private String suffix;

	private boolean leaf;

	private boolean root;

	private List<Node> children;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		setText(nodeName);
		this.nodeName = nodeName;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getParentNode() {
		return parentNode;
	}

	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public String getNodeId() {
		this.nodeId = DigestUtils.md5Hex(filePath);
		setId(nodeId);
		return nodeId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	

}
