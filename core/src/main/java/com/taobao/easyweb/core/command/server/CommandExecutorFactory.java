package com.taobao.easyweb.core.command.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.easyweb.core.command.CommandExecutor;

public class CommandExecutorFactory {

	public static Map<Integer, CommandExecutor> map = new ConcurrentHashMap<Integer, CommandExecutor>();

	public static void regist(Integer type, CommandExecutor executor) {
		map.put(type, executor);
	}

	public static CommandExecutor getExecutor(int type) {
		return map.get(type);
	}

}
