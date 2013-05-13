package com.taobao.easyweb.console.deploy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.agent.ZipUtil;
import com.taobao.easyweb.core.command.Command;
import com.taobao.easyweb.core.command.CommandSender;
import com.taobao.easyweb.core.svn.SVNUtil;

public class DeployManager {

	private DeployTaskDAO deployTaskDAO;

	public static String DEPLOY_APPS_TMP = "/home/admin/easyweb/deploy_tmp/apps/";
	public static String DEPLOY_ZIP_TMP = "/home/admin/easyweb/deploy_tmp/zip/";

	public static String ip;
	static {
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public Result<String> preDeploy(String svnurl, String username, String password, List<String> ips, Date startTime) {
		Result<String> result = new Result<String>(false);
		try {
			String config = SVNUtil.readFile(svnurl, username, password, "app.properties");
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(config.getBytes()));
			String appName = properties.getProperty("app.name");
			String appVersion = properties.getProperty("app.version");
			if (StringUtils.isBlank(appName) || StringUtils.isBlank(appVersion)) {
				result.addErrorMessage("app配置不正确");
				return result;
			}
			String appKey = appName + "-" + appVersion;
			File file = new File(DEPLOY_APPS_TMP + appKey);
			FileUtils.deleteDirectory(file);
			file.mkdirs();
			SVNUtil.doCheckout(svnurl, file.getAbsolutePath(), username, password);

			File zip = new File(DEPLOY_ZIP_TMP + appKey + "-" + System.currentTimeMillis() + ".zip");
			FileUtils.deleteQuietly(zip);
			zip.createNewFile();
			ZipUtil.zip(DEPLOY_APPS_TMP + appKey, zip.getAbsolutePath());

			if (startTime == null) {
				startTime = new Date();
			}
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			IOUtils.copy(new FileInputStream(zip), b);
			String md5 = DigestUtils.md5Hex(b.toByteArray());
			for (String ip : ips) {
				deployHost(appName, appVersion, ip, zip.getAbsolutePath(), md5, startTime);
			}
			result.setSuccess(true);
		} catch (Exception e) {
			result.addErrorMessage("部署异常: " + e.getMessage());
			return result;
		}
		return result;
	}

	private void deployHost(String appName, String appVersion, String ip, String zipFile, String md5, Date startTime) {
		DeployTask task = new DeployTask();
		task.setAppName(appName);
		task.setAppVersion(appVersion);
		task.setDeployHost(ip);
		task.setIp(ip);
		task.setZipMd5(md5);
		task.setStatus(0);
		task.setStartTime(startTime);
		task.setZipFile(zipFile);
		deployTaskDAO.insert(task);
	}

	public void runDeploy() {
		List<DeployTask> tasks = deployTaskDAO.queryMyTask();
		if (tasks.isEmpty()) {
			return;
		}

		for (DeployTask task : tasks) {
			Command command = buildTaskCommand(task);
			//发送数据包
			String response = "";
			
			if(response.startsWith("[success]")){
				
			}
		}
	}

	private Command buildTaskCommand(DeployTask task) {
		Command command = new Command();
		command.setAppKey(task.getAppName() + "-" + task.getAppVersion());
		command.setType(1);

		File file = new File(task.getZipFile());
		if (!file.exists()) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			IOUtils.copy(new FileInputStream(file), out);
			command.setData(out.toByteArray());
			return command;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
