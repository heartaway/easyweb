package com.taobao.easyweb.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.remark.Remark;
import com.taobao.easyweb.remark.RemarkBiz;
import com.taobao.easyweb.remark.RemarkManager;
import com.taobao.easyweb.security.domain.User;

@Component("ewTaskManager")
public class TaskManager {

	@Resource(name = "ewTaskDAO")
	private TaskDAO taskDAO;
	@Resource(name = "ewRemarkManager")
	private RemarkManager remarkManager;
	@Resource(name = "ewUserTaskDAO")
	private UserTaskDAO userTaskDAO;

	// @Resource(name = "ewUserDAO")
	// private UserDAO userDAO;

	public Result<String> saveOrUpdate(Task task) {
		Result<String> result = new Result<String>(true);
		if (task.getId() == null) {
			User user = (User) ThreadContext.getContext().getContext("user");
			if (user != null) {
				task.setCreator(user.getId());
			} else {
				task.setCreator(1);
			}
			if (task.getStatus() == null) {
				task.setStatus(0);
			}
			taskDAO.insert(task);
		} else {
			taskDAO.update(task);
		}
		return result;
	}

	public Result<String> delete(Integer id) {
		Result<String> result = new Result<String>(true);
		Task task = findById(id);
		if (task == null) {
			result.setSuccess(false);
			result.setModule("任务不存在");
			return result;
		}
		task.setStatus(-1);
		taskDAO.update(task);
		return result;
	}

	public List<Task> queryTasks(Task query) {
		List<Task> list = taskDAO.queryTasks(query);
		for (Task task : list) {
			setTaskInfos(task, false);
		}
		return list;
	}

	public Task findById(Integer id) {
		return taskDAO.queryById(id);
	}

	public Task findAllInfo(Integer id) {
		Task task = taskDAO.queryById(id);
		setTaskInfos(task, true);
		return task;
	}

	private void setTaskInfos(Task task, boolean comments) {
		if (task != null) {
			Remark query = new Remark();
			query.setOuterBizId(task.getId());
			query.setOuterBiz(RemarkBiz.TASK_Comments.getValue());
			if (comments) {
				task.setComments(remarkManager.queryRemarks(query));
			}
			// task.setUsers(queryTaskUsers(task.getId()));
			// task.setCreatorName(userDAO.queryById(task.getCreator()).getName());
		}
	}

	// private List<User> queryTaskUsers(Integer taskId) {
	// List<Integer> users = userTaskDAO.queryTaskUsers(taskId);
	// return userDAO.queryByIds(users);
	// }

	public List<Integer> queryTaskUserIds(Integer taskId) {
		return userTaskDAO.queryTaskUsers(taskId);
	}

	/**
	 * 将任务分配给用户，一个或多个
	 * 
	 * @param taskId
	 * @param users
	 * @return
	 */
	public Result<String> assignTo(Integer taskId, List<Integer> users, Date endTime) {
		Result<String> result = new Result<String>(false);
		Task task = findById(taskId);
		if (task == null) {
			result.setModule("任务不存在");
			return result;
		}
		List<Integer> olds = userTaskDAO.queryTaskUsers(taskId);
		for (Integer userId : users) {
			UserTask userTask = queryUserTask(userId, taskId);
			if (userTask == null) {
				userTask = new UserTask();
				userTask.setUserId(userId);
				userTask.setTaskId(taskId);
				if (endTime == null) {
					endTime = task.getEndTime();
				}
				userTask.setEndTime(endTime);
				userTask.setStatus(0);
				userTaskDAO.insert(userTask);
			} else {
				if (userTask.getEndTime() == null || userTask.getEndTime().compareTo(endTime) != 0) {
					userTask.setEndTime(endTime);
					userTaskDAO.update(userTask);
				}
			}
		}
		for (Integer u : olds) {
			if (users.contains(u)) {
				continue;
			}
			UserTask del = new UserTask();
			del.setUserId(u);
			del.setTaskId(taskId);
			userTaskDAO.delete(del);
		}
		result.setSuccess(true);
		return result;
	}

	public Result<String> deleteAssign(Integer userId, Integer taskId) {
		Result<String> result = new Result<String>(true);
		UserTask del = new UserTask();
		del.setUserId(userId);
		del.setTaskId(taskId);
		userTaskDAO.delete(del);
		return result;
	}

	public Result<String> assignToUser(Integer userId, Integer taskId) {
		Result<String> result = new Result<String>(true);
		UserTask userTask = queryUserTask(userId, taskId);
		if (userTask == null) {
			userTask = new UserTask();
			userTask.setUserId(userId);
			userTask.setTaskId(taskId);
			userTask.setEndTime(userTask.getEndTime());
			userTask.setStatus(0);
			userTaskDAO.insert(userTask);
		}
		return result;
	}

	public Result<List<UserTask>> queryUserTasks(Integer userId, Integer status) {
		Result<List<UserTask>> result = new Result<List<UserTask>>(true);
		UserTask query = new UserTask();
		query.setUserId(userId);
		query.setStatus(status);
		result.setModule(userTaskDAO.queryUserTasks(query));
		return result;
	}

	private UserTask queryUserTask(Integer userId, Integer taskId) {
		UserTask query = new UserTask();
		query.setUserId(userId);
		query.setTaskId(taskId);
		List<UserTask> list = userTaskDAO.queryUserTasks(query);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public Result<String> addComment(Integer taskId, String comments) {
		Result<String> result = new Result<String>(false);
		Task task = taskDAO.queryById(taskId);
		if (task == null) {
			result.setModule("任务不存在");
			return result;
		}
		User user = (User) ThreadContext.getContext().getContext("user");
		Remark remark = new Remark();
		remark.setOuterBiz(RemarkBiz.TASK_Comments.getValue());
		remark.setOuterBiz(task.getId());
		remark.setCreatorId(user.getId());
		remark.setContent(comments);
		remarkManager.addRemark(remark);
		return result;
	}

}
