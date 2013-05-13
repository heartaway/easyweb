package com.taobao.easyweb.console.instance;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.console.instance.domain.Instance;
import com.taobao.easyweb.console.instance.domain.InstanceApp;
import com.taobao.easyweb.core.PaginationResult;
import com.taobao.easyweb.core.Result;

@Component("ewInstanceManager")
public class InstanceManager {
	@Resource(name = "ewInstanceDAO")
	private InstanceDAO instanceDAO;
	@Resource(name = "ewInstanceAppDAO")
	private InstanceAppDAO instanceAppDAO;

	public Result<String> addInstance(Instance instance) {
		Result<String> result = new Result<String>(true);
		instanceDAO.insert(instance);
		return result;
	}

	public Result<String> updateInstance(Instance instance) {
		Result<String> result = new Result<String>(true);
		instanceDAO.update(instance);
		return result;
	}

	public Result<List<Instance>> query(Instance instance) {
		Result<List<Instance>> result = new Result<List<Instance>>(true);
		List<Instance> l = instanceDAO.query(instance);
		result.setModule(l);
		return result;
	}

	public PaginationResult<Instance> query(int start, int limit) {
		return instanceDAO.query(start, limit);
	}

	public Result<String> addInstanceApp(InstanceApp instanceApp) {
		Result<String> result = new Result<String>(true);
		instanceAppDAO.insert(instanceApp);
		return result;
	}

	public Result<String> deleteInstanceApp(String appName, String appVersion, List<String> ips) {
		Result<String> result = new Result<String>(true);
		InstanceApp instanceApp = new InstanceApp();
		instanceApp.setAppName(appName);
		instanceApp.setAppVersion(appVersion);
		for (String ip : ips) {
			instanceApp.setIp(ip);
			instanceAppDAO.deleteAppInstance(instanceApp);
		}
		return result;
	}

	public Result<String> updateInstanceApp(InstanceApp instanceApp) {
		Result<String> result = new Result<String>(true);
		instanceAppDAO.update(instanceApp);
		return result;
	}

	public Result<List<InstanceApp>> queryInstanceApp(InstanceApp instanceApp) {
		Result<List<InstanceApp>> result = new Result<List<InstanceApp>>(true);
		List<InstanceApp> l = instanceAppDAO.query(instanceApp);
		result.setModule(l);
		return result;
	}

}
