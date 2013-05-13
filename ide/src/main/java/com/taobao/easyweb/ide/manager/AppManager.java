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
	 * 1����svn�д���Ŀ¼<br>
	 * 2����pom.xml��app.properties�ļ��ϴ�<br>
	 * 3������src/main/javaĿ¼<br>
	 * 4������assetsĿ¼<br>
	 * 5��svn checkout<br>
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
			result.setModule("�ļ�Ŀ¼�Ѿ����� " + path);
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
		result.setModule("�����ɹ�");
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
	 * �ļ�Ӧ���ύ��svnĿ¼
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
			result.setModule("Ŀ¼������");
			return result;
		}
		try {
			createAppSvn(appName, username, password);
			addFolder(appName, folder, username, password);
			SVNUtil.doCommit(new File[] { folder }, "ͨ��web ide�ύ����", new String[0]);
		} catch (Exception e) {
			AppLogger.getLogger().error("�ύsvnʧ�� " + e.getMessage());
			result.setModule("�ύsvn�쳣 " + e.getMessage());
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	private static void addFolder(String appName, File file, String username, String password) {
		if (file.getName().equals("target")) {// target Ŀ¼���ύ
			return;
		}
		if (file.isFile()) {
			String path = DirectoryUtil.getFileParentPath(file).replace(Configuration.getDevPath() + appName, "");
			String svnPath = appSvnBase + "/" + appName + "/" + path;
			try {
				SVNUtil.addFile(svnPath, file.getName(), IOUtils.toByteArray(new FileInputStream(file)), username, password);
			} catch (Exception e) {
				AppLogger.getLogger().error("�ύsvnʧ�� " + e.getMessage());
			}
		} else {
			String path = DirectoryUtil.getFilePath(file).replace(Configuration.getDevPath() + appName, "");
			try {
				SVNUtil.addDirs(appSvnBase + "/" + appName, path, username, password);
			} catch (Exception e) {
				AppLogger.getLogger().error("�ύsvnʧ�� " + e.getMessage());
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
