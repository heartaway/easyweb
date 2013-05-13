package com.taobao.easyweb.repo.svn;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNConflictChoice;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.taobao.easyweb.repo.FileStatus;

public class SVNManager {

	static SVNClientManager clientManager = SVNClientManager.newInstance();

	static SVNStatusClient statusClient = clientManager.getStatusClient();

	/**
	 * 列出目录下的文件状态，在svn提交时弹出界面给出。只列出有变化的文件
	 * 
	 * @param files
	 * @return
	 * @throws SVNException
	 */
	public static List<FileStatus> listFileStatus(File[] files) throws SVNException {
		List<FileStatus> list = new ArrayList<FileStatus>();
		for (File file : files) {
			list(file, list);
		}
		return list;
	}

	private static void list(File file, List<FileStatus> list) throws SVNException {
		SVNStatus status = statusClient.doStatus(file, false);
		if (status.getContentsStatus().getID() != SVNStatusType.STATUS_NORMAL.getID()) {
			FileStatus fileStatus = new FileStatus();
			fileStatus.setFilePath(file.getAbsolutePath());
			fileStatus.setStatus(status.getContentsStatus().toString());
			fileStatus.setDirectory(file.isDirectory());
			list.add(fileStatus);
		}
		if (file.isDirectory()) {
			File[] children = file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return !name.startsWith(".");
				}
			});
			for (File c : children) {
				list(c, list);
			}
		}
	}

	public static List<String> commit(File[] files, String message, boolean commitChildren) throws SVNException {
		List<String> commitMessage = new ArrayList<String>();
		for (File file : files) {
			SVNStatus status = statusClient.doStatus(file, false);
			SVNStatusType statusType = status.getContentsStatus();
			int type = statusType.getID();
			if (type == SVNStatusType.STATUS_UNVERSIONED.getID()) {
				SVNWCClient svnwcClient = clientManager.getWCClient();
				svnwcClient.doAdd(file, true, false, true, SVNDepth.INFINITY, true, true);
				commitMessage.add("Add file " + file.getName() + "...");
				commitMessage.add(commit(file, message));
			} else if (type == SVNStatusType.STATUS_CONFLICTED.getID()) {
				throw new SVNException(SVNErrorMessage.create(SVNErrorCode.WC_CONFLICT_RESOLVER_FAILURE, "文件[" + file.getAbsolutePath() + "]冲突了"));
			} else if (type != SVNStatusType.STATUS_NORMAL.getID()) {
				commitMessage.add(commit(file, message));
			}

			if (file.isDirectory() && commitChildren) {
				File[] children = file.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return !name.startsWith(".");
					}
				});
				commitMessage.addAll(commit(children, message, commitChildren));
			}
		}
		return commitMessage;
	}

	private static String commit(File file, String message) throws SVNException {
		SVNCommitClient commitClient = clientManager.getCommitClient();
		commitClient.doCommit(new File[] { file }, false, message, null, null, true, true, SVNDepth.INFINITY);
		return "Commit " + file.getName();
	}

	public static SVNRepository getSvnRepository(String svnurl, String userName, String pwd) throws SVNException {
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnurl));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
		repository.setAuthenticationManager(authManager);
		return repository;
	}

	public static void checkout(File localPath, String svnurl, String username, String password) throws SVNException {
		DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
		/*
		 * Creates an instance of SVNClientManager providing authentication
		 * information (name, password) and an options driver
		 */
		SVNClientManager clientManager = SVNClientManager.newInstance(options, username, password);
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		if (!localPath.exists()) {
			localPath.mkdirs();
		}
		updateClient.doCheckout(SVNURL.parseURIEncoded(svnurl), localPath, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, true);
	}

	public static void update(File localpath) throws SVNException {
		SVNClientManager clientManager = SVNClientManager.newInstance();
		clientManager.getUpdateClient().doUpdate(localpath, SVNRevision.HEAD, SVNDepth.INFINITY, true, true);
	}

	public static void delete(File file) throws SVNException {
		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNWCClient client = clientManager.getWCClient();
		client.doResolve(file, SVNDepth.INFINITY, SVNConflictChoice.MERGED);
		clientManager.getWCClient().doDelete(file, true, true, true);
		commit(new File[] { file }, "delete", true);
	}

	public static void main(String[] args) throws SVNException {
		// checkout(new File("c:/home/admin/gsp"),
		// "http://code.taobao.org/svn/easyweb/apps/gsp", "shantoong",
		// "shantong");
		// List<String> s = commit(new File[] { new File("c:/home/admin/gsp") },
		// "commit", true);
		// System.out.println(s);
		List<FileStatus> l = listFileStatus(new File[] { new File("c:/home/admin/gsp") });
		System.out.println(l);
		// delete(new File("c:/home/admin/gsp/assets"));
	}

	static {
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}

}
