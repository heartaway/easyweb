package com.taobao.easyweb.core.velocity;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.code.DirectoryUtil;
import com.taobao.easyweb.core.context.Context;
import com.taobao.easyweb.core.context.ThreadContext;

/**
 * Created with IntelliJ IDEA. User: jimmey Date: 12-11-23 Time: ÏÂÎç11:11 To
 * change this template use File | Settings | File Templates.
 */
@Component("ewGroovyVelocityEngine")
public class GroovyVelocityEngine {

	@Resource(name = "ewVelocityEngine")
	private VelocityEngine velocityEngine;

	public String render(String templateName) {
		Context context = ThreadContext.getContext();
		String name = DirectoryUtil.getDirectory(context.getCurrentPath(), templateName).replace(Configuration.getDeployPath(), "");
		return velocityEngine.renderTemplate(name, context.getContextMap());
	}

}
