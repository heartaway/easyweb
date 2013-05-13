package com.taobao.easyweb.task;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

@Component("ewTaskDAO")
public class TaskDAO extends SqlMapClientDaoSupport {

	public void insert(Task task) {
		getSqlMapClientTemplate().insert("taskDAO.insert", task);
	}

	public void update(Task task) {
		getSqlMapClientTemplate().update("taskDAO.update", task);
	}

	public Task queryById(Integer id) {
		return (Task) getSqlMapClientTemplate().queryForObject("taskDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	public List<Task> queryTasks(Task query) {
		return getSqlMapClientTemplate().queryForList("taskDAO.queryTasks", query);
	}
	
}
