package com.taobao.easyweb.appapi;

import groovy.lang.GroovyObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParser;

@Component("ewApiAnnotationParser")
public class ApiAnnotationParser extends AnnotationParser {

	public ApiAnnotationParser() {
		super();
	}

	@Override
	public boolean isParse(Annotation annotation) {
		return (annotation instanceof Api);
	}

	@Override
	public void parse(final App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
		if (!(target instanceof Method)) {
			return;
		}
		Method javaMethod = (Method) target;
		Api api = (Api) annotation;
		ApiMethod apiMethod = new ApiMethod();
		apiMethod.setName(api.value());
		apiMethod.setSigncheck(api.signcheck());
		apiMethod.setFile(file);
		apiMethod.setMethod(javaMethod.getName());
		ApiContainer.regist(app, apiMethod);
	}

}
