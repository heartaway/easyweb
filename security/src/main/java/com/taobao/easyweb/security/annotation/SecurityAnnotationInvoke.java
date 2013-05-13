package com.taobao.easyweb.security.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.taobao.easyweb.core.groovy.annotation.MethodAnnotationInvoke;
import com.taobao.easyweb.security.SecurityManager;

/**
 * 调用的时候执行
 * 
 * @author jimmey
 * 
 */
public class SecurityAnnotationInvoke extends MethodAnnotationInvoke {

	@Override
	public Object invoke(Method method) {
		Annotation[] annotations = method.getAnnotations();
		String[] roles = null, users = null, perms = null;
		for (Annotation an : annotations) {
			if (an instanceof OnRole) {
				roles = ((OnRole) an).value();
			}
			if (an instanceof OnUser) {
				users = ((OnUser) an).value();
			}
			if (an instanceof OnPerm) {
				perms = ((OnPerm) an).value();
			}
		}
		SecurityManager.check(roles, users, perms);
		return null;
	}

}
