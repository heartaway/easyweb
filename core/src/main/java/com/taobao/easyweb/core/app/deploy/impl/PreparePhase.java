//package com.taobao.easyweb.core.app.deploy.impl;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.util.Properties;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.taobao.easyweb.core.Configuration;
//import com.taobao.easyweb.core.app.App;
//import com.taobao.easyweb.core.app.deploy.DeployContext;
//import com.taobao.easyweb.core.app.deploy.Phase;
//import com.taobao.easyweb.core.app.deploy.DeployException;
//import com.taobao.easyweb.core.svn.SVNManager;
//
//public class PreparePhase implements Phase {
//
//	@Override
//	public void process(DeployContext context) throws DeployException {
//		String config;
//		try {
//			config = SVNManager.readFile(context.getSvnurl(), context.getUsername(), context.getPassword(), "app.properties");
//		} catch (Exception e) {
//			throw new DeployException("read app.properties error :" + e.getMessage());
//		}
//		Properties properties = new Properties();
//		try {
//			properties.load(new ByteArrayInputStream(config.getBytes()));
//		} catch (IOException e) {
//			throw new DeployException("paser app.properties error :" + e.getMessage());
//		}
//		String appName = properties.getProperty("app.name");
//		String appVersion = properties.getProperty("app.version");
//		if (StringUtils.isBlank(appName) || StringUtils.isBlank(appVersion)) {
//			throw new DeployException("app name or version error, config: " + config);
//		}
//		String fileName = appName + "-" + appVersion;
//		context.setAppRoot(fileName);
//		context.setAppName(appName);
//		context.setAppVersion(appVersion);
//
//		App app = new App();
//		app.setName(appName);
//		app.setRootPath(Configuration.getDeployPath() + fileName);
//		app.setVersion(appVersion);
//		context.setApp(app);
//	}
//
//}
