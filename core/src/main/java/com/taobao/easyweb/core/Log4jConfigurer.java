package com.taobao.easyweb.core;

import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class Log4jConfigurer {

	static {
		URL url = Log4jConfigurer.class.getClassLoader().getResource("easyweb_log4j.xml");
		DOMConfigurator.configureAndWatch(url.getFile(), 2);
	}

	private static Map<String, Logger> appLoggers = new ConcurrentHashMap<String, Logger>();

	public static Logger getAppLogger(String appKey) {
		Logger logger = appLoggers.get(appKey);
		if (logger != null) {
			return logger;
		}
		Properties properties = new Properties();
		PropertyConfigurator.configure(properties);
		return Logger.getLogger(appKey);
	}

}
