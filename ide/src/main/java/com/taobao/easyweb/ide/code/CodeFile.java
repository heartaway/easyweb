package com.taobao.easyweb.ide.code;

public class CodeFile {

	/**
	 * app�ļ��ĸ�Ŀ¼
	 */
	private String appFilePath;

	/**
	 * �ļ���,��index.groovy
	 */
	private String fileName;

	private String packageName;
	/**
	 * 1���ļ�<br>
	 * 2: Ŀ¼<br>
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
