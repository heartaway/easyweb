package com.taobao.easyweb.core.code.common;

import java.io.StringWriter;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.app.AppContainer;
import com.taobao.easyweb.core.code.CodeRender;
import com.taobao.easyweb.core.context.ThreadContext;

@Component("ewCommonTool")
public class CommonTool {

	@Resource(name = "ewCodeRender")
	private CodeRender codeRender;

	public String include(String app, String name) {
		return include(app, AppContainer.getCurrentVersion(app), name);
	}

	public String include(String appName, String appVersion, String name) {
		String appKey = appName + "-" + appVersion;
		CommonTarget target = CommonContainer.getAppInclude(appName, name);
		if (target == null) {
			return "<!-- import common error " + appKey + " " + name + " -->";
		}
		StringWriter writer = new StringWriter();
		String path = ThreadContext.getContext().getCurrentPath();
		try {
			codeRender.render(target.getGroovyFile(), target.getMethod(), writer);
		} catch (Exception e) {
			return "<!-- import common error " + appKey + " " + name + " " + e.getMessage() + " -->";
		} finally {
			ThreadContext.getContext().setCurrentPath(path);// 把路径回填回去
		}
		return writer.toString();
	}

}
