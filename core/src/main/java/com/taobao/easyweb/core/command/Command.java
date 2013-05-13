package com.taobao.easyweb.core.command;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;

import java.io.UnsupportedEncodingException;

public class Command {

	public static int DEPLOY = 1;

	private int type;

	private String appKey;

	private byte[] data;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setJsonData(Object obj) {
		String json = new JsonBuilder(obj).toString();
		try {
			this.data = json.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getJson() {
		try {
			return new String(this.data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public Object getJsonObj() {
		try {
			return new JsonSlurper().parseText(new String(this.data, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

}
