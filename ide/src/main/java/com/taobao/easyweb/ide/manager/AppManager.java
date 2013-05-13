package com.taobao.easyweb.ide.manager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.code.DirectoryUtil;
import com.taobao.easyweb.core.svn.SVNUtil;

public class AppManager {

	public static String appSvnBase = "http://code.taobao.org/svn/easyweb/apps";

	/**
	 * 1、在svn中创建目录<br>
	 * 2、将pom.xml、app.properties文件上传<br>
	 * 3、创建src/main/java目录<br>
	 * 4、创建assets目录<br>
	 * 5、svn checkout<br>
	 * 
	 * @param name
	 * @param version
	 * @param svnUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public static Result<String> createApp(String name, String version, String svnUrl, String username, String password) {
		Result<String> result = new Result<String>(false);
		String path = Configuration.getDevPath() + name;
		File file = new File(path);
		if (file.exists()) {
			result.setModule("文件目录已经存在 " + path);
			return result;
		}

		App app = new App();
		app.setName(name);
		app.setVersion(version);
		try {
			SVNUtil.addDirs(appSvnBase, name + "/src/main/java", username, password);
			SVNUtil.addDirs(appSvnBase, name + "/src/main/resources", username, password);
			SVNUtil.addDirs(appSvnBase, name + "/assets", username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			SVNUtil.addFile(appSvnBase + "/" + name, "pom.xml", getTemplate("pom.xml", app), username, password);
			SVNUtil.addFile(appSvnBase + "/" + name, "app.properties", getTemplate("app.properties", app), username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			file.mkdirs();
			SVNUtil.doCheckout(appSvnBase + "/" + name, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setSuccess(true);
		result.setModule("创建成功");
		return result;
	}

	private static byte[] getTemplate(String name, App app) {
		try {
			String template = IOUtils.toString(AppManager.class.getClassLoader().getResourceAsStream("template/" + name));
			template = template.replace("${appName}", app.getName());
			template = template.replace("${appVersion}", app.getVersion());
			return template.getBytes();
		} catch (IOException e) {

		}
		return new byte[0];
	}

	/**
	 * 文件应该提交的svn目录
	 * 
	 * @param svnPath
	 * @param folder
	 * @param username
	 * @param password
	 * @return
	 */
	public static Result<String> commitFolder(String appName, String folderPath, String username, String password) {
		Result<String> result = new Result<String>(false);
		File folder = new File(folderPath);
		if (!folder.exists()) {
			result.setModule("目录不存在");
			return result;
		}
		try {
			createAppSvn(appName, username, password);
			addFolder(appName, folder, username, password);
			SVNUtil.doCommit(new File[] { folder }, "通过web ide提交代码", new String[0]);
		} catch (Exception e) {
			AppLogger.getLogger().error("提交svn失败 " + e.getMessage());
			result.setModule("提交svn异常 " + e.getMessage());
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	private static void addFolder(String appName, File file, String username, String password) {
		if (file.getName().equals("target")) {// target 目录不提交
			return;
		}
		if (file.isFile()) {
			String path = DirectoryUtil.getFileParentPath(file).replace(Configuration.getDevPath() + appName, "");
			String svnPath = appSvnBase + "/" + appName + "/" + path;
			try {
				SVNUtil.addFile(svnPath, file.getName(), IOUtils.toByteArray(new FileInputStream(file)), username, password);
			} catch (Exception e) {
				AppLogger.getLogger().error("提交svn失败 " + e.getMessage());
			}
		} else {
			String path = DirectoryUtil.getFilePath(file).replace(Configuration.getDevPath() + appName, "");
			try {
				SVNUtil.addDirs(appSvnBase + "/" + appName, path, username, password);
			} catch (Exception e) {
				AppLogger.getLogger().error("提交svn失败 " + e.getMessage());
			}
			File[] fs = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return !pathname.getName().startsWith(".");
				}
			});
			for (File f : fs) {
				addFolder(appName, f, username, password);
			}
		}

	}

	private static void createAppSvn(String appName, String username, String password) throws Exception {
		try {
			SVNUtil.addDir(appSvnBase, appName, username, password);
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("already exists")) {
			} else {
				throw e;
			}
		}

	}
}
