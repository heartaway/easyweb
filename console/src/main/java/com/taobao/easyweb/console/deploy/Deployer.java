package com.taobao.easyweb.console.deploy;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.agent.ZipUtil;
import com.taobao.easyweb.core.svn.SVNUtil;

public class Deployer {

	public static String DEPLOY_APPS_TMP = "/home/admin/easyweb/deploy_tmp/apps/";
	public static String DEPLOY_ZIP_TMP = "/home/admin/easyweb/deploy_tmp/zip/";

	public static Result<String> deploy(String svnurl, String username, String password,List<String> ips) {
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
			
			File zip = new File(DEPLOY_ZIP_TMP + appKey+".zip");
			FileUtils.deleteQuietly(zip);
			zip.createNewFile();
			ZipUtil.zip(DEPLOY_APPS_TMP + appKey, zip.getAbsolutePath());
			
			result.setSuccess(true);
		} catch (Exception e) {
			result.addErrorMessage("部署异常: "+e.getMessage());
			return result;
		}
		return result;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
//		deploy("http://code.taobao.org/svn/easyweb/apps/ide", "shantoong", "shantong",Arrays.asList("127.0.0.1"));
		String s = "http%3a%2f%2fto%2etaobao%2ecom%2fPOINT%3fbackurl%3dhttps%3a%2f%2fbenefitprod%2ealipay%2ecom%2fredirect%5fdispatch%2ehtm%3fredirectUrl%3dhttp%3a%2f%2fshangcheng365%2ecn%2fi%2f%3fid%3d687474703a2f2f732e636c69636b2e74616f62616f2e636f6d2f743f653d7a475533344341374b253242506b7142303753342532464b304346635266483047374462506b694e394d426568614541424c36524367377148475764724d4a58782532427a5a6d556d69723967416856437373556475774e6a6545627a495759716e624b454d644e6f4e32424f7a685a5a694c5053644a6e4f384d6c77676936376c624765666643713434357a5a644e674e5a414c253246534a34674447386932616831565a394a72774372624e766f64623956396678477373426e7446307569506e53726825324674645957612673706d3d323031342e32313338323738342e312e30";
		System.out.println(URLDecoder.decode(s, "gbk"));
	}

}
