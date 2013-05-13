//package com.taobao.easyweb.core.app.deploy.impl;
//
//import com.taobao.easyweb.core.Configuration;
//import com.taobao.easyweb.core.app.deploy.DeployContext;
//import com.taobao.easyweb.core.app.deploy.Phase;
//import com.taobao.easyweb.core.app.deploy.DeployException;
//import com.taobao.easyweb.core.svn.SVNManager;
//import org.apache.commons.io.IOUtils;
//import org.tmatesoft.svn.core.SVNException;
//
//import java.io.*;
//
///**
// * ��svn�м������<br>
// *
// * @author jimmey
// */
//public class CheckSvnPhase implements Phase {
//
//    // ��ȡsvn��app.properties�ļ�����������ڣ�����ʾ����
//    // ͨ��app.properties�ļ�������Ŀ¼���ļ���Ϊ${appName}-${appVersion}
//    // checkout svn�ļ���������Ŀ¼��
//    @Override
//    public void process(DeployContext context) throws DeployException {
//        File tempFile = new File(Configuration.getDeployTempPath() + context.getAppRoot());
//        File lastDeploy = new File(tempFile.getAbsolutePath() + "-lastDeploy");
//        if (tempFile.exists()) {// ����ļ����Ѿ����ڣ���ɾ��
//            tempFile.deleteOnExit();
//            // ����ϴβ���ʱ��
//            if (!lastDeploy.exists()) {
//                writeLast(lastDeploy, "0");
//            }
//            try {
//                String last = IOUtils.toString(new FileInputStream(lastDeploy));
//                long l = Long.parseLong(last);
//                if (System.currentTimeMillis() - l < 60000) {// �ϴβ���ʱ��С��1����
//                    throw new DeployException("last deploy less than one min");
//                }
//            } catch (Exception e) {
//                throw new DeployException("check lastDeploy error " + e.getMessage());
//            }
//        }
//        tempFile.mkdirs();// ���´����ļ���
//        writeLast(lastDeploy, System.currentTimeMillis() + "");
//        // ��svn�м������
//        try {
//            SVNManager.checkout(tempFile, context.getSvnurl(), context.getUsername(), context.getPassword());
//        } catch (SVNException e) {
//            throw new DeployException("checkout svn error " + e.getMessage());
//        }
//    }
//
//    private void writeLast(File lastDeploy, String time) throws DeployException {
//        try {// ���������ļ�
//            lastDeploy.createNewFile();
//            IOUtils.copy(new ByteArrayInputStream(time.getBytes()), new FileOutputStream(lastDeploy));
//        } catch (IOException e) {
//            throw new DeployException("create lastDeploy error " + e.getMessage());
//        }
//    }
//}
