package com.taobao.easyweb.core.app;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.context.Context;
import com.taobao.easyweb.core.context.ThreadContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA. User: jimmey Date: 12-11-25 Time: ÏÂÎç4:30 To
 * change this template use File | Settings | File Templates.
 */
public class AppLogger {

	public static Map<String, Logger> siteLogger = new ConcurrentHashMap<String, Logger>();

	public static Logger getLogger() {
		return getAppLogger(null);
	}

	public static Logger getLogger(String name) {
		return getAppLogger(name);
	}

	public static Logger getLogger(Class<?> clazz) {
		return getAppLogger(null);
	}

	public static Logger getAppLogger(String name) {
		Context context = ThreadContext.getContext();
		String appKey = "easyweb";
		if (context != null && StringUtils.isNotBlank(context.getAppName())) {
			appKey = context.getAppName();
		}
		return getAppLogger(appKey, name);
	}

	public static Logger getAppLogger(String appKey, String logName) {
		if (logName == null) {
			logName = "easyweb";
		}
		Logger logger = siteLogger.get(appKey);
		if (logger == null) {
			PatternLayout layout = new PatternLayout();
//			layout.setConversionPattern("%d %-5p %c{2} %n%m%n");
            layout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p  %X{requestURI} %c{2} %n%m%n");
			DailyRollingFileAppender appender = new DailyRollingFileAppender();
			appender.setLayout(layout);
			appender.setFile(Configuration.getAppLog(appKey));
			appender.activateOptions();
			appender.setImmediateFlush(true);
			appender.setEncoding("gbk");
			logger = Logger.getLogger(appKey + "-" + logName);
			logger.removeAllAppenders();
			logger.addAppender(appender);
			logger.setLevel((Level) Level.INFO);
			logger.setAdditivity(false);
			siteLogger.put(appKey, logger);
		}
		return logger;
	}

}
