package com.taobao.easyweb.hsf;

import groovy.lang.GroovyObject;

import java.io.File;
import java.lang.annotation.Annotation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.bean.BeanFactory;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParser;

@Component("ewHsfAnnotationParser")
public class HsfAnnotationParser extends AnnotationParser {

	private static volatile boolean isTaeHsf = false;

	public HsfAnnotationParser() {
		super();
		try {
			Class.forName("com.taobao.hsf.ndi.model.ObjectDataWrapper");
			isTaeHsf = true;
		} catch (ClassNotFoundException e) {
			AppLogger.getAppLogger("HSF init").error("hsf版本不正确");
			isTaeHsf = false;
		}
	}

	@Override
	public boolean isParse(Annotation annotation) {
		return (annotation instanceof Hsf) && isTaeHsf;
	}

	@Override
	public void parse(final App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
		Hsf hsf = (Hsf) annotation;
		final String name = hsf.name();
		final String serviceName = hsf.serviceName();
		final String version = (Configuration.isOnline()) ? hsf.onlineVersion() : hsf.dailyVersion();
		if (StringUtils.isBlank(name) || StringUtils.isBlank(serviceName) || StringUtils.isBlank(version)) {
			return;
		}
		groovyObject.setProperty("hsfServiceName", serviceName);
		groovyObject.setProperty("hsfServiceVersion", version);
		// 启用异步线程调用toString注册hsf
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HSFInvokeHelper.invokeHsfService(serviceName, version, "toString", new String[0], new Object[0]);
				} catch (Throwable e) {
					AppLogger.getAppLogger(app.getAppKey(), "HSF").error("init hsf:" + name, e);
				}
			}
		}).start();
		BeanFactory.regist(app, name, groovyObject);
	}

}
