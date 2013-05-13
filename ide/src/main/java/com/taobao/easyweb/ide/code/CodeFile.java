package com.taobao.easyweb.ide.code;

public class CodeFile {

	/**
	 * app文件的根目录
	 */
	private String appFilePath;

	/**
	 * 文件名,如index.groovy
	 */
	private String fileName;

	private String packageName;
	/**
	 * 1：文件<br>
	 * 2: 目录<br>
	 */
	private String templateName;
	
	

	private String content;

	public String getAppFilePath() {
		return appFilePath;
	}

	public void setAppFilePath(String appFilePath) {
		this.appFilePath = appFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
