package com.taobao.easyweb.core.code;

import com.taobao.easyweb.core.Configuration;

import java.io.File;

/**
 * 相对路径处理
 * 
 * @author jimmey
 * 
 */
public class DirectoryUtil {

	public static String FILE_SPLIT = "/";

	/**
	 * 
	 * @param base
	 *            如com/taobao/cmsadmin
	 * @param relativelyFile
	 *            如../backyard/index.vm
	 * @return
	 */
	public static String getDirectory(String base, String relativelyFile) {
		String[] basePath = base.split(FILE_SPLIT);
		String[] targetPath = relativelyFile.split(FILE_SPLIT);
		StringBuilder sb = new StringBuilder();
		int parentDepth = 0;
		for (String s : targetPath) {
			if (s.equals("..")) {
				parentDepth++;
			}
		}
		if (parentDepth >= basePath.length && parentDepth > 0) {
			// 超出了
			return null;
		}
		if (parentDepth == 0 && targetPath.length == 0) {
			sb.append(base).append(FILE_SPLIT).append(relativelyFile);
		} else {
			for (int i = 0; i < basePath.length - parentDepth; i++) {
				sb.append(basePath[i]).append(FILE_SPLIT);
			}
			for (int i = parentDepth; i < targetPath.length; i++) {
				if (!".".equals(targetPath[i]) && !"".equals(targetPath[i])) {
					sb.append(targetPath[i]);
					if (i != targetPath.length - 1) {
						sb.append(FILE_SPLIT);
					}
				}
			}
		}
		return sb.toString();
	}

	public static String getFilePath(File file) {
		return file.getAbsolutePath().replace("\\", "/");
	}

	public static String getFileParentPath(File file) {
		return file.getParent().replace("\\", "/");
	}

	// public static String get

	/**
	 * 根据文件的名称获取app信息
	 * 
	 * @param file
	 * @return
	 */
	public static String getAppName(File file) {

		return "";
	}

	public static String getAppDeployPath(String appName) {
		return Configuration.getDeployPath();
	}

	public static String getAppTempDeployPath(String appName) {
		return Configuration.getDeployPath();
	}

	public static String getAppDeployFile(String appName, String fileName) {
		return getAppDeployPath(appName) + fileName;
	}

	public static String getAppTemplateDeployFile(String appName, String fileName) {
		return getAppTempDeployPath(appName) + fileName;
	}

	public static String getDevPath(String appName, String fileName) {
		return "";
	}

	public static void main(String[] args) {
		System.out.println(getDirectory("com/cmsadmin/aaa", "/aa.vm"));
	}
}
