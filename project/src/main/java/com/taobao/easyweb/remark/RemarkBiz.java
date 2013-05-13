package com.taobao.easyweb.remark;

public enum RemarkBiz {

	PROJECT_Discuss(1),TASK_Comments(2);
	int value;

	private RemarkBiz(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
