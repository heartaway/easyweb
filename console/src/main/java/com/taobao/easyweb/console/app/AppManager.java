package com.taobao.easyweb.console.app;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.console.app.domain.AppInfo;
import com.taobao.easyweb.core.Result;

@Component("ewAppManager")
public class AppManager {

	@Resource(name = "ewAppInfoDAO")
	private AppInfoDAO appInfoDAO;

	public Result<String> addApp(AppInfo appInfo) {
		Result<String> result = new Result<String>(true);
		appInfoDAO.insert(appInfo);
		return result;
	}

	public Result<String> updateApp(AppInfo appInfo) {
		Result<String> result = new Result<String>(true);
		appInfoDAO.update(appInfo);
		return result;
	}

	public Result<AppInfo> queryApp(Integer id) {
		Result<AppInfo> result = new Result<AppInfo>(true);
		result.setModule(appInfoDAO.queryById(id));
		return result;
	}

	public Result<AppInfo> queryApp(String name) {
		Result<AppInfo> result = new Result<AppInfo>(true);
		result.setModule(appInfoDAO.queryByName(name));
		return result;
	}

	public Result<List<AppInfo>> queryApp(AppInfo appInfo) {
		Result<List<AppInfo>> result = new Result<List<AppInfo>>(true);
		result.setModule(appInfoDAO.query(appInfo));
		return result;
	}

	public List<AppInfo> queryAllApps() {
		return appInfoDAO.queryAll();
	}

}
