package com.taobao.easyweb.ide.code;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.Result;

public class CodeDeploy {

	public static String DEPLOY_PATH = Configuration.getDeployPath();

	/**
	 * 发布执行的动作包括：<br>
	 * 
	 * @param path
	 * @return
	 */
	public Result<String> deploy(String appName, String appVersion) {
		Result<String> result = new Result<String>();
		
		return result;
	}

}
