package com.taobao.easyweb.core.bean;

import java.util.Map;

public class LocalBean {
	private Map<String, String> localBeans;
	private Boolean allLocalBeans = false;

	public Map<String, String> getLocalBeans() {
		return localBeans;
	}

	public void setLocalBeans(Map<String, String> localBeans) {
		this.localBeans = localBeans;
	}

	public Boolean getAllLocalBeans() {
		return allLocalBeans;
	}

	public void setAllLocalBeans(Boolean allLocalBeans) {
		this.allLocalBeans = allLocalBeans;
	}

}
