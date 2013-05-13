package com.taobao.easyweb.console.app;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.console.app.domain.AppInfo;

@Component("ewAppInfoDAO")
public class AppInfoDAO extends SqlMapClientDaoSupport {

	public void insert(AppInfo appInfo) {
		getSqlMapClientTemplate().insert("appInfoDAO.insert",appInfo);
	}

	public void update(AppInfo appInfo) {
		getSqlMapClientTemplate().update("appInfoDAO.update",appInfo);
	}

	public AppInfo queryById(Integer id) {
		return (AppInfo) getSqlMapClientTemplate().queryForObject("appInfoDAO.queryById",id);
	}
	
	public AppInfo queryByName(String name) {
		return (AppInfo) getSqlMapClientTemplate().queryForObject("appInfoDAO.queryByName",name);
	}

	@SuppressWarnings("unchecked")
	public List<AppInfo> query(AppInfo appInfo) {
		return getSqlMapClientTemplate().queryForList("appInfoDAO.query",appInfo);
	}
	
	@SuppressWarnings("unchecked")
	public List<AppInfo> queryAll() {
		return getSqlMapClientTemplate().queryForList("appInfoDAO.queryAll");
	}

}
