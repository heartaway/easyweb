package com.taobao.easyweb.security;

import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.taobao.easyweb.core.PaginationResult;
import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.security.annotation.OnPerm;
import com.taobao.easyweb.security.dao.PermissionDAO;
import com.taobao.easyweb.security.dao.RoleDAO;
import com.taobao.easyweb.security.dao.UserDAO;
import com.taobao.easyweb.security.domain.Permission;
import com.taobao.easyweb.security.domain.Role;
import com.taobao.easyweb.security.domain.User;

@Component("ewSecurityManager")
public class SecurityManager {

	@Resource(name = "ewUserDAO")
	private UserDAO userDAO;
	@Resource(name = "ewRoleDAO")
	private RoleDAO roleDAO;
	@Resource(name = "ewPermissionDAO")
	private PermissionDAO permissionDAO;

	// private Logger logger = LoggerUtil.getLogger();

	public static Map<String, List<MethodSecurity>> securities = new ConcurrentHashMap<String, List<MethodSecurity>>();
	public static Map<String, OnPerm> methodOnPerms = new ConcurrentHashMap<String, OnPerm>();

	public static void add(Script script, String method, List<String> keys) {
		modified = true;
		for (String key : keys) {
			MethodSecurity methodSecurity = new MethodSecurity();
			methodSecurity.setKey(key);
			methodSecurity.setClassName(script.getClass().getSimpleName());
			methodSecurity.setPackageName(script.getClass().getPackage().getName());
			methodSecurity.setMessages(keys.toString());
			methodSecurity.setMethod(method);
			add(key, methodSecurity);
		}
	}

	public static void remove(Script script, String method, List<String> keys) {
		for (String key : keys) {
			MethodSecurity methodSecurity = new MethodSecurity();
			methodSecurity.setKey(key);
			methodSecurity.setClassName(script.getClass().getSimpleName());
			methodSecurity.setPackageName(script.getClass().getPackage().getName());
			methodSecurity.setMessages(keys.toString());
			methodSecurity.setMethod(method);
			if (securities.containsKey(key)) {
				securities.get(key).remove(methodSecurity);
			}
		}
	}

	private static void add(String key, MethodSecurity methodSecurity) {
		if (!securities.containsKey(key)) {
			securities.put(key, new ArrayList<MethodSecurity>());
		}
		securities.get(key).add(methodSecurity);
	}

	/**
	 * 注册权限对象，在代码解析的时候调用
	 * 
	 * @param key
	 * @param description
	 * @return
	 */
	public Permission regist(String key, String description) {
		Permission permission = permissionDAO.queryByKey(key);
		if (permission != null) {
			return permission;
		}
		permission = new Permission();
		permission.setKey(key);
		permission.setDescription(description);
		permissionDAO.save(permission);
		return permission;
	}

	/**
	 * 暂时只删除权限对象，不删除关联权限
	 * 
	 * @param permissionId
	 * @return
	 */
	public boolean deletePermission(Integer permissionId) {
		int size = permissionDAO.delete(permissionId);
		return size > 0;
	}

	public Result<String> addUserPermission(Integer userId, Integer permissionId) {
		Result<String> result = new Result<String>(false);
		Permission permission = permissionDAO.queryById(permissionId);
		if (permission == null) {
			result.addErrorMessage("权限不存在 id=" + permissionId);
			return result;
		}
		User user = userDAO.queryById(userId);
		if (user == null) {
			result.addErrorMessage("用户不存在 id=" + userId);
			return result;
		}
		result.setSuccess(true);
		userDAO.applyPermission(userId, permissionId);
		userPermissions.refresh(userId);
		return result;
	}

	public void addUserRole(Integer userId, Integer roleId) {
		userDAO.addRole(userId, roleId);
		userPermissions.refresh(userId);
	}

	public void deleteUserRole(Integer userId, Integer roleId) {
		userDAO.deleteRole(userId, roleId);
		userPermissions.refresh(userId);
	}

	public boolean removeUserPermission(Integer userId, Integer permissionId) {
		userDAO.removePermission(userId, permissionId);
		userPermissions.refresh(userId);
		return true;
	}

	public boolean addRolePermission(Integer roleId, Integer permissionId) {
		Permission permission = permissionDAO.queryById(permissionId);
		if (permission == null) {
			return false;
		}
		Role role = roleDAO.queryById(roleId);
		if (role == null) {
			return false;
		}
		roleDAO.applyPermission(roleId, permissionId);
		refreshRole(roleId);
		return true;
	}

	public boolean removeRolePermission(Integer roleId, Integer permissionId) {
		roleDAO.removePermission(roleId, permissionId);
		refreshRole(roleId);
		return true;
	}

	private void refreshRole(Integer roleId) {
		List<Integer> users = queryRoleUserIds(roleId);
		for (Integer user : users) {
			userPermissions.refresh(user);
		}
	}

	public List<Permission> queryRolePermissions(Integer roleId) {
		List<Integer> ids = roleDAO.queryRolePermissions(roleId);
		return permissionDAO.queryByIds(ids);
	}

	public List<Integer> queryRolePermissionIds(Integer roleId) {
		return roleDAO.queryRolePermissions(roleId);
	}

	public Role queryRoleInfo(Integer roleId) {
		return roleDAO.queryById(roleId);
	}

	public Result<String> addOrUpdateRole(Role role) {
		Result<String> result = new Result<String>(false);
		if (StringUtils.isBlank(role.getName())) {
			result.addErrorMessage("角色名称不能为空");
			return result;
		}
		Role tmp = roleDAO.queryByName(role.getName());
		if (tmp == null && role.getId() == null) {
			roleDAO.save(role);
			result.setSuccess(true);
			return result;
		}

		if (role.getId() != null && tmp == null) {
			tmp = new Role();
			tmp.setId(role.getId());
			tmp.setName(role.getName());
			tmp.setDescription(role.getDescription());
			roleDAO.update(tmp);
		}
		result.setSuccess(true);
		return result;
	}

	public List<User> queryRoleUsers(Integer roleId) {
		List<Integer> ids = roleDAO.queryRoleUsers(roleId);
		return userDAO.queryByIds(ids);
	}

	public List<Integer> queryRoleUserIds(Integer roleId) {
		return roleDAO.queryRoleUsers(roleId);
	}

	public User queryUserByName(String name) {
		User user = null;
		try {
			user = userCaches.get(name);
			if (user.getName() == null) {
				user = null;
			}
		} catch (ExecutionException e) {
			// logger.error(e.getMessage());
		}
		if (user == null) {
			user = new User();
			user.setName(name);
			try {
				userDAO.save(user);
			} catch (Throwable e) {
			}
			user = userDAO.queryByName(name);
			userCaches.refresh(name);
		}
		return user;
	}

	public User queryUserAllInfo(String name) {
		User user = queryUserByName(name);
		if (user == null) {
			return null;
		}
		user.setPermissions(queryUserPermissionKeys(user.getId()).keySet());
		user.setRoles(queryUserRoles(user.getId()));
		return user;
	}

	public User createUser(String name) {
		User user = userDAO.queryByName(name);
		if (user != null) {
			return user;
		}
		user = new User();
		user.setName(name);
		userDAO.save(user);
		return userDAO.queryByName(name);
	}

	public Role createRole(String name, String desc) {
		Role role = roleDAO.queryByName(name);
		if (role != null) {
			return role;
		}
		role = new Role();
		role.setName(name);
		role.setDescription(desc);
		roleDAO.save(role);
		return roleDAO.queryByName(name);
	}

	/**
	 * 查询用户权限，用户从角色继承过来的权限也在这里体现。来源将在source说明中给出
	 * 
	 * @param userId
	 * @return
	 */
	public List<Permission> queryUserPermissions(Integer userId) {
		try {
			return userPermissions.get(userId);
		} catch (ExecutionException e) {
			return Collections.emptyList();
		}
	}

	public Map<String, Permission> queryUserPermissionKeys(Integer userId) {
		List<Permission> permissions = queryUserPermissions(userId);
		Map<String, Permission> permission = new HashMap<String, Permission>();
		for (Permission perm : permissions) {
			permission.put(perm.getKey(), perm);
		}
		return permission;
	}

	public Map<String, Permission> queryUserPermissionKeys(String authName) {
		User user = queryUserByName(authName);
		if (user != null) {
			return queryUserPermissionKeys(user.getId());
		}
		return Collections.emptyMap();
	}

	public List<Role> queryUserRoles(Integer userId) {
		List<Integer> roles = userDAO.queryUserRoles(userId);
		return roleDAO.queryByIds(roles);
	}

	public PaginationResult<Permission> queryPermissions(int pageNumber) {
		PaginationResult<Permission> result = new PaginationResult<Permission>(pageNumber, 10);
		result.setTotalSize(permissionDAO.queryCount());
		result.setModule(permissionDAO.queryPages(result.getStart(), result.getPageSize()));
		return result;
	}

	public PaginationResult<User> queryUsers(int pageNumber) {
		PaginationResult<User> result = new PaginationResult<User>(pageNumber, 10);
		result.setTotalSize(userDAO.queryCount());
		result.setModule(userDAO.queryAll(result.getStart(), result.getPageSize()));
		return result;
	}

	public PaginationResult<Role> queryRoles(int pageNumber) {
		PaginationResult<Role> result = new PaginationResult<Role>(pageNumber, 10);
		result.setTotalSize(roleDAO.queryCount());
		result.setModule(roleDAO.queryAll(result.getStart(), result.getPageSize()));
		return result;
	}

	public void initSecurity(HttpServletRequest request) {
		String name = (String) request.getAttribute("authName");
		if (name == null) {
			return;
		}
		User user = queryUserAllInfo(name);
		ThreadContext.getContext().putContext("authUser", user);
	}

	public LoadingCache<String, User> userCaches = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<String, User>() {
		public User load(String name) throws Exception {
			User user = userDAO.queryByName(name);
			if (user == null) {
				user = new User();// 空的用户
			}
			return user;
		}
	});

	public LoadingCache<Integer, List<Permission>> userPermissions = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<Integer, List<Permission>>() {
		public List<Permission> load(Integer userId) throws Exception {
			User user = userDAO.queryById(userId);
			if (user == null) {
				return Collections.emptyList();//
			}
			Set<Integer> permissionIds = new HashSet<Integer>();
			List<Integer> userRoles = userDAO.queryUserRoles(user.getId());
			List<Integer> rolePermissions = new ArrayList<Integer>();
			if (!userRoles.isEmpty()) {
				rolePermissions = roleDAO.queryRolesPermissions(userRoles);
				permissionIds.addAll(rolePermissions);
			}
			List<Integer> userPermissions = userDAO.queryUserPermissions(user.getId());
			permissionIds.addAll(userPermissions);

			List<Permission> permissions = permissionDAO.queryByIds(new ArrayList<Integer>(permissionIds));
			for (Permission permission : permissions) {
				if (rolePermissions.contains(permission.getId())) {
					permission.setDescription(permission.getDescription() + ";来自角色的权限");
				}
				if (userPermissions.contains(permission.getId())) {
					permission.setDescription(permission.getDescription() + ";来自用户的权限");
				}
			}
			return permissions;
		}
	});

	private static volatile boolean modified = false;

	Thread syncThread = new Thread(new Runnable() {
		public void run() {
			while (true) {
				try {
					if (modified) {
						for (String key : securities.keySet()) {
							regist(key, "系统同步");
						}
						modified = false;
					}
					Thread.sleep(3000);
				} catch (Throwable e) {
				}
			}
		}
	});

	@PostConstruct
	public void init() {
		syncThread.start();
	}

	public static void check(String[] roles, String[] users, String[] perms) {
		User user = (User) ThreadContext.getContext().getContext("authUser");
		if (user == null) {
			throw new SecurityException("没有用户对象");
		}
		if (roles != null && roles.length > 0) {
			for (String r : roles) {
				if (user.getRoleNames().contains(r)) {
					return;
				}
			}
		}
		if (users != null && users.length > 0) {
			for (String r : users) {
				if (user.getRoleNames().contains(r)) {
					return;
				}
			}
		}
		if (perms != null && perms.length > 0) {
			Set<String> userPermissions = user.getPermissions();
			for (String p : perms) {
				if (!userPermissions.contains(p)) {
					throw new SecurityException("no permission " + p);
				}
			}
		}
	}

}
