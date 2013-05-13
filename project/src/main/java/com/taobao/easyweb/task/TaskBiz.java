package com.taobao.easyweb.task;

public enum TaskBiz {
	PROJECT_Task(1);
	int value;

	private TaskBiz(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
