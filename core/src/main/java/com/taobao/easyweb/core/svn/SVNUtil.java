package com.taobao.easyweb.core.svn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Properties;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.internal.wc.SVNCommitMediator;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNWorkspaceMediator;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * svn kit http://svnkit.com/javadoc/index.html
 */
public class SVNUtil {

	static {
		Properties properties = System.getProperties();  
        properties.setProperty("svnkit.http.methods", "Basic,Digest,NTLM");  
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}

	public static SVNRepository getSvnRepository(String svnurl, String userName, String pwd) throws Exception {
		try {
			SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnurl));
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
			repository.setAuthenticationManager(authManager);
			return repository;
		} catch (SVNException e) {
			throw e;
		}
	}

	public static String readFile(String svnurl, String userName, String pwd, String fileName) throws Exception {
		return readFile(getSvnRepository(svnurl, userName, pwd), fileName);
	}

	public static String readFile(SVNRepository repository, String fileName) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SVNProperties fileProperties = new SVNProperties();
		try {
			SVNNodeKind nodeKind = repository.checkPath(fileName, -1);
			if (nodeKind == SVNNodeKind.NONE) {
				throw new Exception("svn节点不存在");
			} else if (nodeKind == SVNNodeKind.DIR) {
				throw new Exception("svn节点为目录");
			}
			repository.getFile(fileName, -1, fileProperties, baos);
			return new String(baos.toByteArray(), "GBK");
		} catch (SVNException e) {
			throw e;
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
	}

	/**
	 * 提交单级目录 目录已存在会抛如下异常 SVNException: svn: Path '/repos/tae/trunk/xx' already
	 * exists
	 * 
	 * @param svnPath
	 * @param dirPath
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static boolean addDir(String svnPath, String dirPath, String username, String password) throws Exception {
		ISVNWorkspaceMediator isvnWorkspaceMediator = new SVNCommitMediator(Collections.emptyMap());
		SVNRepository repository = getSvnRepository(svnPath, username, password);
		ISVNEditor editor = repository.getCommitEditor("create dir by program", isvnWorkspaceMediator);
		editor.openRoot(-1);
		editor.addDir(dirPath, null, -1);
		editor.closeDir();
		return editor.closeEdit().getNewRevision() > 0;
	}

	/**
	 * 提交多级目录 循环提交
	 * 
	 * @param svnPath
	 * @param username
	 * @param password
	 * @param dirPaths
	 *            形如aaa/bbb/ccc
	 * @throws org.tmatesoft.svn.core.SVNException
	 * 
	 */
	public static boolean addDirs(String svnPath, String dirPaths, String username, String password) throws Exception {
		String[] paths = dirPaths.split("/");
		String last = "";
		if (svnPath.endsWith("/")) {
			svnPath = svnPath.substring(0, svnPath.length() - 1);
		}
		for (int i = 0; i < paths.length; i++) {
			boolean suc = false;
			try {
				suc = addDir(svnPath + last, paths[i], username, password);
			} catch (Exception e) {
				// 已存在当作成功处理
				if (e.getMessage() != null && e.getMessage().contains("already exists")) {
					suc = true;
				} else {
					throw e;
				}
			}
			if (!suc) {
				return false;
			}
			last = last + "/" + paths[i];
		}
		return true;
	}

	/**
	 * 提交文件
	 * 
	 * @param svnPath
	 * @param fileName
	 *            形如zzz.txt
	 * @param data
	 *            文件数据
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean addFile(String svnPath, String fileName, byte[] data, String username, String password) throws Exception {
		SVNRepository repository = getSvnRepository(svnPath, username, password);
		SVNNodeKind nodeKind = repository.checkPath(fileName, -1);
		if (nodeKind == SVNNodeKind.FILE) {// 文件已经存在
			return true;
		}
		ISVNWorkspaceMediator isvnWorkspaceMediator = new SVNCommitMediator(Collections.emptyMap());
		ISVNEditor editor = repository.getCommitEditor("create file by program", isvnWorkspaceMediator);
		editor.openRoot(-1);
		editor.addFile(fileName, null, -1);
		editor.applyTextDelta(fileName, null);
		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(fileName, new ByteArrayInputStream(data), editor, true);
		editor.closeFile(fileName, checksum);
		return editor.closeEdit().getNewRevision() > 0;
	}

	/**
	 * http://svnkit.com/javadoc/index.html
	 * 
	 * @param svnPath
	 * @param filePath
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static long doCheckout(String svnPath, String filePath) throws Exception {
		return doCheckout(svnPath, filePath, null, null);
	}

	/**
	 * http://svnkit.com/javadoc/index.html
	 * 
	 * @param svnPath
	 * @param filePath
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static long doCheckout(String svnPath, String filePath, String username, String password) throws Exception {
		SVNClientManager clientManager = null;
		if (username != null && password != null) {
			clientManager = SVNClientManager.newInstance(new DefaultSVNOptions(), username, password);
		} else {
			clientManager = SVNClientManager.newInstance();
		}
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		SVNURL url = SVNURL.parseURIEncoded(svnPath);
		File file = new File(filePath);
		return updateClient.doCheckout(url, file, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, true);
	}

	public static long doUpdate(String filePath) throws Exception {
		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		File file = new File(filePath);
		return updateClient.doUpdate(file, SVNRevision.HEAD, SVNDepth.INFINITY, true, true);
	}

	public static long doCommit(File[] paths, String commitMessage, String[] changelists) throws Exception {
		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNCommitClient commitClient = clientManager.getCommitClient();
		SVNCommitInfo info = commitClient.doCommit(paths, false, commitMessage, null, changelists, true, true, SVNDepth.INFINITY);
		return info.getNewRevision();
	}

	// public static void a(){
	// SVNClientManager clientManager = SVNClientManager.newInstance();
	// SVNChangelistClient changelistClient =
	// clientManager.getChangelistClient();
	//
	// SVNStatusClient statusClient = clientManager.getStatusClient();
	// try {
	// SVNStatus status = statusClient.doStatus(new File(""), false);
	//
	// } catch (SVNException e) {
	// e.printStackTrace();
	// }
	//
	// SVNWCClient svnwcClient = clientManager.getWCClient();
	//
	// SVNDiffClient diffClient = clientManager.getDiffClient();
	// }

	public static void main(String[] v) {
		// String svnPath =
		// "https://opensvn.taobao.com:80/repos/sitemodules/backyard/src/main";
		// String localFile = "C:\\Users\\jimmey\\workspace\\test";
		// try {
		// // boolean a = doCheckout(svnPath, localFile, "shantong",
		// "shantong");
		// // System.out.print(a);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

}
