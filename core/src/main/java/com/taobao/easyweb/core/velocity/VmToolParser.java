package com.taobao.easyweb.core.velocity;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParser;
import groovy.lang.GroovyObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.annotation.Annotation;

@Component
public class VmToolParser extends AnnotationParser {

	@Override
	public boolean isParse(Annotation annotation) {
		return annotation instanceof VmTool;
	}

	@Override
	public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
		VmTool vmTool = (VmTool) annotation;
		if (StringUtils.isNotBlank(vmTool.value())) {
			VmToolFactory.putAppTool(app.getAppKey(), vmTool.value(), groovyObject);
		}
	}

}
