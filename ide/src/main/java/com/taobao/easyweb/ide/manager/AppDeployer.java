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
 * Ӧ�÷����߼����������߼�<br>
 * 1��app
 * 
 * @author jimmey
 * 
 */
public class AppDeployer {

	/**
	 * App������Ŀ¼��ַ��ȫ��ͳһ
	 */
	public static String APP_DEPLOY_TEMP_PATH = "/home/admin/easyweb/deploy/temp/";

	public static String APP_DEPLOY_PATH = "/home/admin/easyweb/deploy/apps";

	/**
	 * ����Ԥ����,������ȡsvn��app�����ļ�,�����ļ�����,�ж������ķ����
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
			result.addErrorMessage("��ȡapp.propertiesʧ��");
			return result;
		}

		String depedencies = properties.getProperty("depedencies", "");
		List<String> msgs = new ArrayList<String>();
		for (String d : depedencies.split(",")) {
			d = d.trim();
			if (BeanFactory.getBean(d) == null) {
				msgs.add("����[" + d + "]�����ڣ����ܽ��в���");
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
	 * ��������Ӧ��
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
				result.addErrorMessage("checkout svnʧ��");
				return result;
			}
		} else {
			try {
				SVNUtil.doUpdate(file.getAbsolutePath());
			} catch (Exception e) {
				result.addErrorMessage("����svnʧ��");
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
					throw new IOException("�ļ�[" + file.getName() + "]����ʧ��");
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
	 * ���𵥸��ļ�
	 * 
	 * @param appName
	 * @param fileName
	 */
	public Result<String> deployFile(String appName, String fileName) {
		Result<String> result = new Result<String>(false);

		String editorPath = DirectoryUtil.getDevPath(appName, fileName);
		File file = new File(editorPath);
		if (!file.exists()) {
			result.addErrorMessage("�ļ�������");
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
				result.addErrorMessage("���������ļ�ʧ��");
				return result;
			}
		}

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(target);
			IOUtils.copy(new FileInputStream(file), fileOutputStream);
			IOUtils.closeQuietly(fileOutputStream);
		} catch (Exception e) {
			result.addErrorMessage("���Ʒ����ļ�ʧ��");
			return result;
		}
		result.setSuccess(true);
		result.setModule("�����ļ�[" + fileName + "]�ɹ�");
		return result;
	}

	/**
	 * ж��һ��Ӧ��,��Ҫ��<br>
	 * 1. ��app��Ŀ¼ɾ��<br>
	 * 2. ɾ��app����ע���@Bean <br>
	 * 3. ɾ��app����ע���@Page <br>
	 * 4. ɾ��app����ע���@Hsf
	 * 
	 * @param appInfo
	 */
	public Result<String> unDeploy(String appName) {
		Result<String> result = new Result<String>(true);
		// AppDeploy.undeploy(appName);// �Ȱ�ҳ���µ�
		// File file = new File(APP_DEPLOY_TEMP_PATH + appName);
		// file.deleteOnExit();// ɾ���ļ���
		// result.setModule("ж�سɹ�");
		return result;
	}

}
