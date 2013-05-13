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
// * 从svn中检出代码<br>
// *
// * @author jimmey
// */
//public class CheckSvnPhase implements Phase {
//
//    // 读取svn的app.properties文件，如果不存在，则提示错误
//    // 通过app.properties文件，创建目录，文件名为${appName}-${appVersion}
//    // checkout svn文件到创建的目录下
//    @Override
//    public void process(DeployContext context) throws DeployException {
//        File tempFile = new File(Configuration.getDeployTempPath() + context.getAppRoot());
//        File lastDeploy = new File(tempFile.getAbsolutePath() + "-lastDeploy");
//        if (tempFile.exists()) {// 如果文件夹已经存在，则删除
//            tempFile.deleteOnExit();
//            // 检查上次部署时间
//            if (!lastDeploy.exists()) {
//                writeLast(lastDeploy, "0");
//            }
//            try {
//                String last = IOUtils.toString(new FileInputStream(lastDeploy));
//                long l = Long.parseLong(last);
//                if (System.currentTimeMillis() - l < 60000) {// 上次部署时间小于1分钟
//                    throw new DeployException("last deploy less than one min");
//                }
//            } catch (Exception e) {
//                throw new DeployException("check lastDeploy error " + e.getMessage());
//            }
//        }
//        tempFile.mkdirs();// 重新创建文件夹
//        writeLast(lastDeploy, System.currentTimeMillis() + "");
//        // 从svn中检出代码
//        try {
//            SVNManager.checkout(tempFile, context.getSvnurl(), context.getUsername(), context.getPassword());
//        } catch (SVNException e) {
//            throw new DeployException("checkout svn error " + e.getMessage());
//        }
//    }
//
//    private void writeLast(File lastDeploy, String time) throws DeployException {
//        try {// 创建部署文件
//            lastDeploy.createNewFile();
//            IOUtils.copy(new ByteArrayInputStream(time.getBytes()), new FileOutputStream(lastDeploy));
//        } catch (IOException e) {
//            throw new DeployException("create lastDeploy error " + e.getMessage());
//        }
//    }
//}
