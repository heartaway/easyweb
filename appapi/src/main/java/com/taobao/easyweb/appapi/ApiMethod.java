package com.taobao.easyweb.appapi;

import java.io.File;

public class ApiMethod {

	private String name;
	
	private File file;
	
	private String method;
	
	private boolean signcheck;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isSigncheck() {
		return signcheck;
	}

	public void setSigncheck(boolean signcheck) {
		this.signcheck = signcheck;
	}
	
	
	
}
