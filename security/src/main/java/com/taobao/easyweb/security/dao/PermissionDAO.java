package com.taobao.easyweb.security.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.security.domain.Permission;

@Component("ewPermissionDAO")
public class PermissionDAO extends SqlMapClientDaoSupport {

	public int delete(Integer id) {
		return getSqlMapClientTemplate().delete("permissionDAO.delete", id);
	}

	public void save(Permission permission) {
		getSqlMapClientTemplate().insert("permissionDAO.save", permission);
	}

	public Permission queryById(Integer id) {
		return (Permission) getSqlMapClientTemplate().queryForObject("permissionDAO.queryById", id);
	}

	public Permission queryByKey(String key) {
		return (Permission) getSqlMapClientTemplate().queryForObject("permissionDAO.queryByKey", key);
	}

	@SuppressWarnings("unchecked")
	public List<Permission> queryByIds(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}
		return getSqlMapClientTemplate().queryForList("permissionDAO.queryMapByIds", ids);
	}
	
	@SuppressWarnings("unchecked")
	public List<Permission> queryPages(int start,int limit){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("limit", limit);
		return getSqlMapClientTemplate().queryForList("permissionDAO.queryPages", map);
	}

	public int queryCount(){
		return (Integer)getSqlMapClientTemplate().queryForObject("permissionDAO.queryCount");
	}
}
