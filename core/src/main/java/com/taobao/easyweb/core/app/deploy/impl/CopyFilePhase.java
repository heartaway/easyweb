//package com.taobao.easyweb.core.app.deploy.impl;
//
//import com.taobao.easyweb.core.Configuration;
//import com.taobao.easyweb.core.app.deploy.DeployContext;
//import com.taobao.easyweb.core.app.deploy.Phase;
//import com.taobao.easyweb.core.app.deploy.DeployException;
//import com.taobao.easyweb.core.code.DirectoryUtil;
//import org.apache.commons.io.IOUtils;
//
//import java.io.*;
//
///**
// * ���׶Σ�copy����<br>
// * ���ĳ���ļ�copy����,Ҫ��ô����?
// *
// * @author jimmey
// */
//public class CopyFilePhase implements Phase {
//
//    FileFilter fileFilter = new FileFilter() {
//        @Override
//        public boolean accept(File pathname) {
//            return !pathname.getName().equals(".svn");
//        }
//    };
//
//    @Override
//    public void process(DeployContext context) throws DeployException {
//        // ����app version��ʽĿ¼
//        File appRoot = new File(Configuration.getDeployPath() + context.getAppRoot());
//        if (!appRoot.exists()) {// ���������,�򴴽�;������ɾ������
//            appRoot.mkdirs();
//        }
//        File tempFile = new File(Configuration.getDeployTempPath() + context.getAppRoot());
//        if (!tempFile.exists()) {
//            throw new DeployException("temp path not exist");
//        }
//        copyToDeploy(tempFile);
//
//    }
//
//    private void copyToDeploy(File file) throws DeployException {
//        String deployPath = DirectoryUtil.getFilePath(file).replace(Configuration.getDeployTempPath(), Configuration.getDeployPath());
//        File deployFile = new File(deployPath);
//        if (file.isDirectory()) {
//            if (!deployFile.exists()) {
//                deployFile.mkdirs();
//            }
//            File[] children = file.listFiles(fileFilter);
//            for (File c : children) {
//                copyToDeploy(c);
//            }
//        } else {
//            if (!deployFile.exists()) {
//                try {
//                    deployFile.createNewFile();
//                } catch (IOException e) {
//                    throw new DeployException("create deploy file error: " + deployFile.getAbsolutePath());
//                }
//            }
//            try {
//                IOUtils.copy(new FileInputStream(file), new FileOutputStream(deployFile));
//            } catch (Exception e) {
//                throw new DeployException("copy deploy file error: " + deployFile.getAbsolutePath());
//            }
//        }
//    }
//
//}
