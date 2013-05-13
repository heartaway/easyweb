package com.taobao.easyweb.ide.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.bean.BeanFactory;
import com.taobao.easyweb.core.code.DirectoryUtil;
import com.taobao.easyweb.core.svn.SVNUtil;

/**
 * 应用发布逻辑，有以下逻辑<br>
 * 1、app
 * 
 * @author jimmey
 * 
 */
public class AppDeployer {

	/**
	 * App发布的目录地址，全部统一
	 */
	public static String APP_DEPLOY_TEMP_PATH = "/home/admin/easyweb/deploy/temp/";

	public static String APP_DEPLOY_PATH = "/home/admin/easyweb/deploy/apps";

	/**
	 * 部署预处理,包括读取svn的app配置文件,解析文件内容,判断依赖的服务等
	 * 
	 * @param appInfo
	 * @return
	 */
	private Result<String> preDeploy(App appInfo) {
		Result<String> result = new Result<String>(false);
		Properties properties = new Properties();
		try {
//			String config = SVNUtil.readFile(appInfo.getSvnUrl(), appInfo.getSvnUsername(), appInfo.getSvnPassword(), "app.properties");
//			InputStream input = new ByteArrayInputStream(config.getBytes());
//			properties.load(input);
		} catch (Exception e) {
			result.addErrorMessage("读取app.properties失败");
			return result;
		}

		String depedencies = properties.getProperty("depedencies", "");
		List<String> msgs = new ArrayList<String>();
		for (String d : depedencies.split(",")) {
			d = d.trim();
			if (BeanFactory.getBean(d) == null) {
				msgs.add("服务[" + d + "]不存在，不能进行部署");
			}
		}
		if (!msgs.isEmpty()) {
			result.setErrorMessages(msgs);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 部署整个应用
	 * 
	 * @param appInfo
	 */
	public Result<String> deploy(App appInfo) {
		Result<String> result = preDeploy(appInfo);
		if (!result.isSuccess()) {
			return result;
		}

		File file = new File(APP_DEPLOY_TEMP_PATH + appInfo.getName());
		if (!file.exists()) {
			file.mkdirs();
			try {
				SVNUtil.doCheckout(appInfo.getSvnUrl(), file.getAbsolutePath());
			} catch (Exception e) {
				result.addErrorMessage("checkout svn失败");
				return result;
			}
		} else {
			try {
				SVNUtil.doUpdate(file.getAbsolutePath());
			} catch (Exception e) {
				result.addErrorMessage("更新svn失败");
				return result;
			}
		}
		try {
			copyToDeploy(file);
		} catch (IOException e) {
			result.addErrorMessage(e.getMessage());
			return result;
		}
		return result;
	}

	private void copyToDeploy(File file) throws IOException {
		File target = new File(file.getAbsolutePath().replace(APP_DEPLOY_TEMP_PATH, APP_DEPLOY_PATH));
		if (file.isFile()) {
			if (!target.exists()) {
				try {
					target.createNewFile();
				} catch (IOException e) {
					throw new IOException("文件[" + file.getName() + "]发布失败");
				}
				FileOutputStream fileOutputStream = new FileOutputStream(target);
				IOUtils.copy(new FileInputStream(file), fileOutputStream);
				IOUtils.closeQuietly(fileOutputStream);
			}
		} else {
			if (!target.exists()) {
				target.mkdirs();
			}
		}
	}

	/**
	 * 部署单个文件
	 * 
	 * @param appName
	 * @param fileName
	 */
	public Result<String> deployFile(String appName, String fileName) {
		Result<String> result = new Result<String>(false);

		String editorPath = DirectoryUtil.getDevPath(appName, fileName);
		File file = new File(editorPath);
		if (!file.exists()) {
			result.addErrorMessage("文件不存在");
			return result;
		}
		String filePath = DirectoryUtil.getAppDeployFile(appName, fileName);
		File target = new File(filePath);
		if (!target.exists()) {
			String parent = filePath.substring(0, filePath.lastIndexOf("/"));
			File p = new File(parent);
			if (!p.exists()) {
				p.mkdirs();
			}
			try {
				target.createNewFile();
			} catch (IOException e) {
				result.addErrorMessage("创建发布文件失败");
				return result;
			}
		}

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(target);
			IOUtils.copy(new FileInputStream(file), fileOutputStream);
			IOUtils.closeQuietly(fileOutputStream);
		} catch (Exception e) {
			result.addErrorMessage("复制发布文件失败");
			return result;
		}
		result.setSuccess(true);
		result.setModule("发布文件[" + fileName + "]成功");
		return result;
	}

	/**
	 * 卸载一个应用,需要做<br>
	 * 1. 将app的目录删除<br>
	 * 2. 删除app所有注册的@Bean <br>
	 * 3. 删除app所有注册的@Page <br>
	 * 4. 删除app所有注册的@Hsf
	 * 
	 * @param appInfo
	 */
	public Result<String> unDeploy(String appName) {
		Result<String> result = new Result<String>(true);
		// AppDeploy.undeploy(appName);// 先把页面下掉
		// File file = new File(APP_DEPLOY_TEMP_PATH + appName);
		// file.deleteOnExit();// 删除文件夹
		// result.setModule("卸载成功");
		return result;
	}

}
