package com.taobao.easyweb.core;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {

	private boolean success;

	private T module;

	public Result() {
	}

	public Result(boolean success) {
		this.success = success;
	}

	private List<String> errorMessages;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getModule() {
		return module;
	}

	public void setModule(T module) {
		this.module = module;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public void addErrorMessage(String msg) {
		if (errorMessages == null) {
			errorMessages = new ArrayList<String>();
		}
		this.errorMessages.add(msg);
	}

}
