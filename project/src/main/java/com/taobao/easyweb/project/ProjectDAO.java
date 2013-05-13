package com.taobao.easyweb.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

@Component("ewProjectDAO")
public class ProjectDAO extends SqlMapClientDaoSupport {

	public void insert(Project project) {
		getSqlMapClientTemplate().insert("projectDAO.insert", project);
	}

	public void update(Project project) {
		getSqlMapClientTemplate().update("projectDAO.update", project);
	}

	public Project queryById(Integer id) {
		return (Project) getSqlMapClientTemplate().queryForObject("projectDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> queryUserProjects(Integer userId) {
		return getSqlMapClientTemplate().queryForList("projectDAO.queryUserProjects", userId);
	}

	@SuppressWarnings("unchecked")
	public List<Project> queryProjects(List<Integer> ids) {
		return getSqlMapClientTemplate().queryForList("projectDAO.queryProjects", ids);
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> queryChildrenProjects(Integer id) {
		return getSqlMapClientTemplate().queryForList("projectDAO.queryChildrenProjects", id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> queryAllProjects() {
		return getSqlMapClientTemplate().queryForList("projectDAO.queryAllProjects");
	}

	public void addUser(Integer projectId, Integer userId) {
		Map<String, Integer> up = new HashMap<String, Integer>();
		up.put("userId", userId);
		up.put("projectId", projectId);
		getSqlMapClientTemplate().insert("projectDAO.addUser", up);
	}

	public void removeUser(Integer projectId, Integer userId) {
		Map<String, Integer> up = new HashMap<String, Integer>();
		up.put("userId", userId);
		up.put("projectId", projectId);
		getSqlMapClientTemplate().update("projectDAO.removeUser", up);
	}

}
