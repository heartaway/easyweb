package com.taobao.easyweb.ide.log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.taobao.easyweb.core.Configuration;

/**
 * 跟进上下文环境获取对应app的Logger
 * 
 * @author jimmey
 * 
 */
public class AppLogger {

	static Map<String, Integer> fileLine = new HashMap<String, Integer>();

	public static List<String> getLogLines(String appKey) {
		try {
			List<String> lines = IOUtils.readLines(new FileInputStream(Configuration.getLogRoot() + appKey + ".log"));
			Integer start = fileLine.get(appKey);
			if (start == null) {
				start = 0;
			}
			fileLine.put(appKey, lines.size());
			if (lines.size() > start) {
				return lines.subList(start, lines.size());
			} else {
				return lines;
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return Collections.emptyList();
	}

}
