package com.taobao.easyweb.project;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.remark.Remark;
import com.taobao.easyweb.remark.RemarkBiz;
import com.taobao.easyweb.remark.RemarkDAO;
import com.taobao.easyweb.security.domain.User;
import com.taobao.easyweb.task.Task;
import com.taobao.easyweb.task.TaskManager;

@Component("ewProjectManager")
public class ProjectManager {

	@Resource(name = "ewProjectDAO")
	private ProjectDAO projectDAO;
	@Resource(name = "ewRemarkDAO")
	private RemarkDAO remarkDAO;
	@Resource(name = "ewTaskManager")
	private TaskManager taskManager;

	public static Integer TASK_TYPE = 1;
	public static Integer TASK_OPTION = 2;
	public static Integer DISCUSS_TYPE = 3;

	public Result<String> saveOrUpdate(Project project) {
		Result<String> result = new Result<String>(true);
		if (project.getId() == null) {
			User user = (User) ThreadContext.getContext().getContext("user");
			if (user != null) {
				project.setCreator(user.getId());
			} else {
				project.setCreator(1);
			}
			projectDAO.insert(project);
		} else {
			projectDAO.update(project);
		}

		return result;
	}

	public Result<List<Project>> queryUserProjects(Integer userId) {
		Result<List<Project>> result = new Result<List<Project>>(true);
		List<Integer> ids = projectDAO.queryUserProjects(userId);
		result.setModule(projectDAO.queryProjects(ids));
		return result;
	}

	public Result<Project> queryProject(Integer id) {
		Result<Project> result = new Result<Project>(true);
		result.setModule(projectDAO.queryById(id));
		return result;
	}

	public Result<List<Project>> queryAllProject() {
		Result<List<Project>> result = new Result<List<Project>>(true);
		result.setModule(projectDAO.queryAllProjects());
		return result;
	}

	public Result<String> addProjectUser(Integer projectId, Integer userId) {
		Result<String> result = new Result<String>(true);
		projectDAO.addUser(projectId, userId);
		return result;
	}

	public Result<String> removeProjectUser(Integer projectId, Integer userId) {
		Result<String> result = new Result<String>(true);
		projectDAO.removeUser(projectId, userId);
		return result;
	}

	public List<Task> queryFinishedTasks(Integer projectId) {
		Task query = new Task();
		query.setOuterBizId(projectId);
		query.setOuterBiz(1);
		query.setStatus(1);
		return taskManager.queryTasks(query);
	}

	public List<Task> queryUnfinishedTasks(Integer projectId) {
		Task query = new Task();
		query.setOuterBizId(projectId);
		query.setOuterBiz(1);
		query.setStatus(0);
		return taskManager.queryTasks(query);
	}

	public List<Remark> queryDiscuss(Integer projectId) {
		Remark query = new Remark();
		query.setOuterBizId(projectId);
		query.setOuterBiz(RemarkBiz.PROJECT_Discuss.getValue());
		return remarkDAO.queryRemarks(query);
	}

	public Result<String> addDiscuss(Remark remark) {
		Result<String> result = new Result<String>();
		remark.setOuterBiz(RemarkBiz.PROJECT_Discuss.getValue());
		remarkDAO.insert(remark);
		return result;
	}

}
