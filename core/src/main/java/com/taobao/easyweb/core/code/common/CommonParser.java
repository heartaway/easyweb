package com.taobao.easyweb.core.code.common;

import groovy.lang.GroovyObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParser;

@Component("ewCommonParser")
public class CommonParser extends AnnotationParser {

	@Override
	public boolean isParse(Annotation annotation) {
		return annotation instanceof Common;
	}

	@Override
	public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
		Method javaMethod = (Method) target;
		Common common = javaMethod.getAnnotation(Common.class);
		if (common == null) {// 只支持没有参数的方法
			return;
		}
		CommonTarget commonTarget = new CommonTarget();
		commonTarget.setGroovyFile(file);
		commonTarget.setMethod(javaMethod.getName());
		commonTarget.setName(common.value());
		CommonContainer.addCommonCode(app, commonTarget);
	}

}
