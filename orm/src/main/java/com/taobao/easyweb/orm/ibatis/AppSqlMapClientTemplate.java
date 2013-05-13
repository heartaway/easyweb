package com.taobao.easyweb.orm.ibatis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibatis.sqlmap.client.SqlMapClient;

public class AppSqlMapClientTemplate {

	public static Map<String, SqlMapClient> sqlmapClients = new ConcurrentHashMap<String, SqlMapClient>();

	public SqlMapClient sqlMapClient() {
		return null;
	}

}
