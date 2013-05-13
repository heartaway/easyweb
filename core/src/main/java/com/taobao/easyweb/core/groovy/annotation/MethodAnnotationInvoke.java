package com.taobao.easyweb.core.groovy.annotation;

import java.lang.reflect.Method;

public abstract class MethodAnnotationInvoke {

	public MethodAnnotationInvoke() {
		MethodAnnotationInvokeFactory.regist(this.getClass().getName(), this);
	}

	public abstract Object invoke(Method method);

}
