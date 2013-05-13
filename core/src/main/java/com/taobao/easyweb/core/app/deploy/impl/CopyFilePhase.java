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
// * 最后阶段，copy代码<br>
// * 如果某个文件copy出错,要怎么处理?
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
//        // 创建app version正式目录
//        File appRoot = new File(Configuration.getDeployPath() + context.getAppRoot());
//        if (!appRoot.exists()) {// 如果不存在,则创建;否则不做删除操作
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
