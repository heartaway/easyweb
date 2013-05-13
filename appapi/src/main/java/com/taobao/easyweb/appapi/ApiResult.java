package com.taobao.easyweb.appapi;

import java.util.ArrayList;
import java.util.List;

public class ApiResult {

	private String api;

	private boolean success;

	private List<String> errorMessages = new ArrayList<String>(2);

	public Object data;

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void addErrorMessage(String msg) {
		this.errorMessages.add(msg);
	}

}
