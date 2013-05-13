package com.taobao.easyweb.security.annotation;

import groovy.lang.GroovyObject;
import groovy.lang.Script;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParser;
import com.taobao.easyweb.security.SecurityManager;

/**
 * ��groovy���������а�ȫ���ע��ʱ����Ϣ��ͬ��
 * 
 * @author jimmey
 * 
 */
public class SecurityAnnotationParser extends AnnotationParser {

	@Override
	public boolean isParse(Annotation annotation) {
		return (annotation instanceof OnPerm) || (annotation instanceof OnRole) || (annotation instanceof OnUser);
	}

	@Override
	public void parse(App app,Annotation annotation, File file, Object target, GroovyObject groovyObject) {
		if (annotation instanceof OnPerm) {
			Method method = (Method) target;
			parseOnPerm((Script) groovyObject, method.getName(), (OnPerm) annotation);
		}
	}

	private static Map<String, OnPerm> methodOnPerms = new HashMap<String, OnPerm>();

	private static void parseOnPerm(Script script, String method, OnPerm security) {
		String methodKey = script.getClass().getName() + method;
		OnPerm old = methodOnPerms.get(methodKey);
		if (old == null) {// ֱ������µ�
			methodOnPerms.put(methodKey, security);
			SecurityManager.add(script, methodKey, Arrays.asList(security.value()));
		} else {// ���ϵ�û�з����仯������Ҫ��������
			List<String> toDelete = new ArrayList<String>(Arrays.asList(old.value()));
			toDelete.removeAll(Arrays.asList(security.value()));
			List<String> toAdd = new ArrayList<String>(Arrays.asList(security.value()));
			toAdd.removeAll(Arrays.asList(old.value()));
			if (!toDelete.isEmpty() || !toAdd.isEmpty()) {
				methodOnPerms.put(methodKey, security);
				SecurityManager.remove(script, method, toDelete);
				SecurityManager.add(script, method, toAdd);
			}
		}
	}

}
