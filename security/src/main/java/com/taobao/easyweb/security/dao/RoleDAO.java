package com.taobao.easyweb.security.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.security.domain.Role;
import com.taobao.easyweb.security.domain.RoleUserPermission;

@Component("ewRoleDAO")
public class RoleDAO extends SqlMapClientDaoSupport {

	public Role queryById(Integer id) {
		return (Role) getSqlMapClientTemplate().queryForObject("roleDAO.queryById", id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> queryAll() {
		return getSqlMapClientTemplate().queryForList("roleDAO.queryAll");
	}

	public void save(Role role) {
		getSqlMapClientTemplate().insert("roleDAO.save", role);
	}
	
	public void update(Role role) {
		getSqlMapClientTemplate().update("roleDAO.update", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> queryByIds(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}
		return getSqlMapClientTemplate().queryForList("roleDAO.queryByIds", ids);
	}

	public Role queryByName(String name) {
		return (Role) getSqlMapClientTemplate().queryForObject("roleDAO.queryByName", name);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> queryRolesPermissions(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}
		return getSqlMapClientTemplate().queryForList("rolePermissionDAO.queryRolesPermissions", ids);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> queryRolePermissions(Integer roleId) {
		return getSqlMapClientTemplate().queryForList("rolePermissionDAO.queryRolePermissions", roleId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> queryAll(int start,int limit) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("limit", limit);
		return getSqlMapClientTemplate().queryForList("roleDAO.queryAll",map);
	}

	public int applyPermission(Integer roleId, Integer permissionId) {
		RoleUserPermission userPermission = new RoleUserPermission();
		userPermission.setPermissionId(permissionId);
		userPermission.setType(2);
		userPermission.setRoleOrUserId(roleId);
		userPermission.setStatus(0);
		getSqlMapClientTemplate().insert("roleUserPermissionDAO.save", userPermission);
		return 0;
	}
	
	public int removePermission(Integer userId, Integer permissionId) {
		RoleUserPermission userPermission = new RoleUserPermission();
		userPermission.setPermissionId(permissionId);
		userPermission.setType(2);
		userPermission.setRoleOrUserId(userId);
		getSqlMapClientTemplate().delete("roleUserPermissionDAO.delete", userPermission);
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> queryRoleUsers(Integer roleId) {
		return getSqlMapClientTemplate().queryForList("roleDAO.queryRoleUsers", roleId);
	}
	
	public int queryCount(){
		return (Integer)getSqlMapClientTemplate().queryForObject("roleDAO.queryCount");
	}
}
