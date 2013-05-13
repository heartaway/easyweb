package com.taobao.easyweb.ide.code;

import groovy.json.JsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.code.DirectoryUtil;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.core.svn.SVNManager;
import com.taobao.easyweb.security.domain.User;

/**
 * 代码编辑，只有在日常或开发环境可以调用<br>
 * 
 * 在本地文件系统开发，不依赖外部服务<br>
 * 
 * 需要判断开发权限
 * 
 * @author jimmey
 * 
 */
public class CodeEditor {

	public static Result<String> addFile(String parentPath, String fileName, int fileType, Map<String, String> templateParams) {
		Result<String> result = new Result<String>(false);
		File file = new File(parentPath);
		if (!file.exists()) {
			result.setModule("父文件不存在");
			return result;
		}
		/**
		 * fileType.put(0, "folder") fileType.put(1, "groovy") fileType.put(2,
		 * "vm") fileType.put(3, "js") fileType.put(4, "css")
		 */
		String suffix = "";
		switch (fileType) {
		case 0:// 文件夹
			file = new File(parentPath + "/" + fileName);
			file.mkdirs();
			result.setModule("创建目录成功");
			result.setSuccess(true);
			return result;
		case 1:// groovy
			suffix = ".groovy";
			break;
		case 2:// groovy
			suffix = ".vm";
			break;
		case 3:// groovy
			suffix = ".js";
			break;
		case 4:// groovy
			suffix = ".css";
			break;
		}
		try {
			byte[] code = getTemplate(templateParams);
			file = new File(parentPath + "/" + fileName + suffix);
			file.createNewFile();
			IOUtils.copy(new ByteArrayInputStream(code), new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			result.setModule("创建文件失败 " + e.getMessage());
		} catch (IOException e) {
			result.setModule("创建文件失败 " + e.getMessage());
		}
		result.setSuccess(true);
		result.setModule("创建成功");
		return result;
	}

	private static LoadingCache<String, String> templateCaches = CacheBuilder.newBuilder().maximumSize(10).expireAfterWrite(1, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
		public String load(String key) throws Exception {
			String code = "";
			try {
				code = IOUtils.toString(CodeEditor.class.getClassLoader().getResourceAsStream("template/" + key));
			} catch (Exception e) {
			}
			return code;
		}
	});

	private static byte[] getTemplate(Map<String, String> params) {
		String key = params.get("template");
		try {
			String code = templateCaches.get(key);
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					code = code.replace("${" + entry.getKey() + "}", entry.getValue());
				}
			}
			return code.getBytes();
		} catch (ExecutionException e) {
			return new byte[0];
		}
	}

	public Result<List<Node>> getEditAppTree() {
		Result<List<Node>> result = new Result<List<Node>>(true);
		String editPath = getEditPath();
		File file = new File(editPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		result.setModule(buildPathTree(editPath));
		return result;
	}

	public Result<List<Node>> getDeployAppTree() {
		Result<List<Node>> result = new Result<List<Node>>(true);
		String editPath = getEditPath();
		File file = new File(editPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		result.setModule(buildPathTree(editPath));
		return result;
	}

	private String getEditPath() {
		User user = (User) ThreadContext.getContext().getContextMap().get("authUser");
		if (user == null) {// 用户不存在
			return null;
		}
		return Configuration.getDevPath() + user.getName();
	}

	private List<Node> buildPathTree(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] apps = file.listFiles();
		List<Node> trees = new ArrayList<Node>();
		for (File app : apps) {
			trees.add(buildTree(app));
		}
		return trees;
	}

	public static void main(String[] args) {
		CodeEditor codeEditor = new CodeEditor();
		File file = new File("C:/Users/jimmey/workspace/ew/easyweb/app");
		Node tree = codeEditor.buildTree(file);
		System.out.println(new JsonBuilder(tree).toString());
	}

	public static void print(Node node) {
		System.out.println(node.getNodeName());
		if (node.getChildren() != null) {
			for (Node c : node.getChildren()) {
				print(c);
			}
		}
	}

	public String stringTree() {
		File file = new File(getDevPath());
		Node tree = buildTree(file);
		return new JsonBuilder(tree).toString();
	}

	private static String getDevPath() {
		String path = Configuration.getDevPath();
		User user = (User) ThreadContext.getContext().getContext("user");
		if (user != null) {
			path = path + user.getName();
		}
		return path;
	}

	public Node buildTree(File file) {
		Node tree = new Node();
		tree.setNodeName(file.getName());
		tree.setFilePath(DirectoryUtil.getFilePath(file));
		tree.setRoot(true);
		File[] children = listFiles(file);
		List<Node> nodes = new ArrayList<Node>();
		for (File f : children) {
			if (f.getName().equals("assets")) {// 公共assets不索引进来
				continue;
			}
			buildNode(f, file.getName(), nodes);
		}
		for (Node n : nodes) {
			// delete(n);
			visitTree(n, n.getNodeName());
		}
		tree.setChildren(nodes);

		return tree;
	}

	private void visitTree(Node treeNode, String appName) {
		treeNode.setAppName(appName);
		if (treeNode.getParentNode() != null) {
			treeNode.setNodeName(treeNode.getFilePath().replace(treeNode.getParentNode() + "/", ""));
		}
		if (!treeNode.isLeaf()) {
			for (Node node : treeNode.getChildren()) {
				visitTree(node, appName);
			}
		}
	}

	private void buildNode(File file, String parentName, List<Node> nodes) {
		if (DirectoryUtil.getFileParentPath(file).contains("/target/") || file.getName().equals("target")) {
			return;
		}
		Node node = new Node();
		node.setParentNode(DirectoryUtil.getFileParentPath(file));
		node.setFilePath(DirectoryUtil.getFilePath(file));
		node.setNodeName(file.getName());
		// String filePath = DirectoryUtil.getFilePath(file);
		if (file.isFile()) {
			node.setLeaf(true);
			node.setSuffix(getSuffix(file));
			nodes.add(node);
		} else {
			node.setLeaf(false);
			File[] children = listFiles(file);
			List<Node> Nodes = new ArrayList<Node>();
			for (File c : children) {
				buildNode(c, node.getNodeName(), Nodes);
			}
			node.setChildren(Nodes);
			nodes.add(node);
		}
	}

	private String getSuffix(File file) {
		String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		if ("vm".equals(suffix)) {
			return "velocity";
		}
		if ("js".equals(suffix)) {
			return "javascript";
		}
		if ("properties".equals(suffix)) {
			return "text";
		}
		return suffix;
	}

	private File[] listFiles(File file) {
		return file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return !pathname.getName().startsWith(".");
			}
		});
	}

	public static Result<String> importApp(String svnurl, String username, String password) {
		Result<String> result = new Result<String>(false);
		try {
			String config = SVNManager.readFile(svnurl, username, password, "app.properties");
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(config.getBytes()));
			String appName = properties.getProperty("app.name");
			if (StringUtils.isBlank(appName)) {
				result.addErrorMessage("应用名称为空");
				return result;
			}
			File dir = new File(getDevPath() + appName);
			if(dir.exists()){
				result.addErrorMessage("应用目录已经存在");
				return result;
			}
			dir.mkdirs();
			SVNManager.checkout(dir, svnurl, username, password);
			result.setSuccess(true);
			result.setModule("导入成功");
		} catch (Exception e) {
			result.addErrorMessage("读取应用配置信息出错 " + e.getMessage());
			return result;
		}
		return result;
	}

}
