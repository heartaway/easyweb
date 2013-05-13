package com.taobao.easyweb.security.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.security.domain.RoleUserPermission;
import com.taobao.easyweb.security.domain.User;

@Component("ewUserDAO")
public class UserDAO extends SqlMapClientDaoSupport {

	public User queryById(Integer id) {
		return (User) getSqlMapClientTemplate().queryForObject("userDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	public List<User> queryByIds(List<Integer> ids) {
		return getSqlMapClientTemplate().queryForList("userDAO.queryByIds", ids);
	}

	public User queryByName(String name) {
		return (User) getSqlMapClientTemplate().queryForObject("userDAO.queryByName", name);
	}

	@SuppressWarnings("unchecked")
	public List<User> queryAll(int start, int limit) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("limit", limit);
		return getSqlMapClientTemplate().queryForList("userDAO.queryAll", map);
	}

	public int queryCount() {
		return (Integer) getSqlMapClientTemplate().queryForObject("userDAO.queryCount");
	}

	public void save(User user) {
		getSqlMapClientTemplate().insert("userDAO.save", user);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> queryUserPermissions(Integer userId) {
		return getSqlMapClientTemplate().queryForList("userPermissionDAO.queryUserPermissions", userId);
	}

	public int applyPermission(Integer userId, Integer permissionId) {
		RoleUserPermission userPermission = new RoleUserPermission();
		userPermission.setPermissionId(permissionId);
		userPermission.setType(1);
		userPermission.setStatus(0);
		userPermission.setRoleOrUserId(userId);
		getSqlMapClientTemplate().insert("roleUserPermissionDAO.save", userPermission);
		return 0;
	}

	public int removePermission(Integer userId, Integer permissionId) {
		RoleUserPermission userPermission = new RoleUserPermission();
		userPermission.setPermissionId(permissionId);
		userPermission.setType(1);
		userPermission.setRoleOrUserId(userId);
		getSqlMapClientTemplate().delete("roleUserPermissionDAO.delete", userPermission);
		return 0;
	}

	public void addRole(Integer userId, Integer roleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("roleId", roleId);
		if (queryUserRole(userId, roleId) != null) {
			return;
		}
		getSqlMapClientTemplate().insert("userDAO.addRole", map);
	}
	
	public Integer queryUserRole(Integer userId, Integer roleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("roleId", roleId);
		return (Integer) getSqlMapClientTemplate().queryForObject("userDAO.queryUserRole", map);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> queryUserRoles(Integer userId) {
		return getSqlMapClientTemplate().queryForList("userDAO.queryUserRoles", userId);
	}

	public void deleteRole(Integer userId, Integer roleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("roleId", roleId);
		getSqlMapClientTemplate().insert("userDAO.deleteRole", map);
	}

}
