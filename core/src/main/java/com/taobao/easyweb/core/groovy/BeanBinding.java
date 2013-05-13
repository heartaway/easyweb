package com.taobao.easyweb.core.groovy;

import groovy.lang.Binding;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.bean.BeanFactory;

@Component("ewBinding")
public class BeanBinding extends Binding {

	@Override
	public Object getVariable(String name) {
		Object obj = BeanFactory.getBean(name);
		if (obj != null) {
			return obj;
		}
		return super.getVariable(name);
	}

	@Override
	public Object getProperty(String property) {
		Object obj = BeanFactory.getBean(property);
		if (obj != null) {
			return obj;
		}
		return super.getProperty(property);
	}

}
