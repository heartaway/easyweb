package com.taobao.easyweb.core.app.deploy.process;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.app.deploy.DeployException;
import com.taobao.easyweb.core.app.deploy.DeployPhase;
import com.taobao.easyweb.core.app.deploy.Deployer;
import com.taobao.easyweb.core.app.scanner.ScanResult;
import com.taobao.easyweb.core.groovy.groovyobject.AppClassLoaderFactory;
import com.taobao.easyweb.core.groovy.groovyobject.GroovyObjectLoader;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.FileSystemCompiler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午2:55
 */
@Deployer(DeployPhase.COMPILE_GROOVY)
@Component
public class BizProcessor extends FileProcessor {

    @Resource
    private GroovyObjectLoader groovyObjectLoader;

    @Override
    public void process(ScanResult result) throws DeployException {
        App app = result.getApp();
        List<String> groovyCode = result.getBizGroovyFiles();
        String appClassPath = Configuration.getClasspath(app);

//        try {
//            FileUtils.deleteDirectory(new File(appClassPath));
//            FileUtils.deleteQuietly(new File(appClassPath));
//            FileUtils.forceDeleteOnExit(new File(appClassPath));
//        } catch (Exception e) {
//            AppLogger.getAppLogger(app.getAppKey(), "Groovy-Compile").error(e);
//        }
        deleteDirectory(app, new File(appClassPath));

        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setTargetDirectory(appClassPath);
        FileSystemCompiler compiler = new FileSystemCompiler(configuration);
        try {
            compiler.compile(groovyCode.toArray(new String[groovyCode.size()]));
        } catch (Exception e) {
            //todo 如果编译出错，则应用启动失败
            AppLogger.getAppLogger(app.getAppKey(), "Groovy-Compile").error(e);
            throw new DeployException("Complie Error", e);
        }

        try {
            /**
             * 设置biz classloader的classpath
             */
            GroovyClassLoader classLoader = AppClassLoaderFactory.getAppBizClassLoader(app);
            classLoader.addClasspath(appClassPath);
            /**
             * 先实例化groovy对象
             */
            for (String file : groovyCode) {
                groovyObjectLoader.instanceObject(false, app, new File(file));
            }
            /**
             * 处理对象注入
             */
            for (String file : groovyCode) {
                groovyObjectLoader.autowiredObject(false, app, new File(file));
            }
        } catch (Exception e) {
            AppLogger.getAppLogger(app.getAppKey(), "Groovy-Init").error(e);
            throw new DeployException("Init Error", e);
        }
    }

    private void deleteDirectory(App app, File file) {
        try {
            if (file.isFile()) {
                file.delete();
            } else {
                for (File c : file.listFiles()) {
                    deleteDirectory(app, c);
                }
            }
        } catch (Exception e) {
            AppLogger.getAppLogger(app.getAppKey(), "Groovy-Compile").error(e);
        }
    }
}
