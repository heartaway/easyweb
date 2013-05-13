package com.taobao.easyweb.core.code.common;

import java.util.HashMap;
import java.util.Map;

import com.taobao.easyweb.core.app.App;

public class CommonContainer {

	private  static Map<String, Map<String, CommonTarget>> map = new HashMap<String, Map<String, CommonTarget>>();

	public static void addCommonCode(App app, CommonTarget target) {
		getAppIncludes(app.getName()).put(target.getName(), target);
	}

	private static Map<String, CommonTarget> getAppIncludes(String app) {
		Map<String, CommonTarget> r = map.get(app);
		if (r == null) {
			r = new HashMap<String, CommonTarget>();
			map.put(app, r);
		}
		return r;
	}

	public static CommonTarget getAppInclude(String app, String includeName) {
		return getAppIncludes(app).get(includeName);
	}

}
