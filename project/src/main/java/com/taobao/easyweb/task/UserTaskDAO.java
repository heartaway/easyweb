package com.taobao.easyweb.task;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

@Component("ewUserTaskDAO")
public class UserTaskDAO extends SqlMapClientDaoSupport {

	public void insert(UserTask task) {
		getSqlMapClientTemplate().insert("userTaskDAO.insert", task);
	}

	public void update(UserTask task) {
		getSqlMapClientTemplate().update("userTaskDAO.update", task);
	}

	public void delete(UserTask task) {
		getSqlMapClientTemplate().delete("userTaskDAO.delete", task);
	}

	@SuppressWarnings("unchecked")
	public List<UserTask> queryUserTasks(UserTask userTask) {
		return getSqlMapClientTemplate().queryForList("userTaskDAO.queryTasks", userTask);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> queryTaskUsers(Integer taskId) {
		return getSqlMapClientTemplate().queryForList("userTaskDAO.queryTaskUsers", taskId);
	}

}
