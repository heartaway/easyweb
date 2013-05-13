package com.taobao.easyweb.core.app.deploy.process;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.app.deploy.DeployException;
import com.taobao.easyweb.core.app.deploy.DeployPhase;
import com.taobao.easyweb.core.app.deploy.Deployer;
import com.taobao.easyweb.core.app.scanner.ScanResult;
import com.taobao.easyweb.core.groovy.groovyobject.GroovyObjectLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午2:55
 */
@Deployer(DeployPhase.INIT_GROOVYOBJECT)
@Component
public class WebProcessor extends FileProcessor {

    @Resource
    private GroovyObjectLoader groovyObjectLoader;

    @Override
    public void process(ScanResult result) throws DeployException {
        App app = result.getApp();
        List<String> groovyCode = result.getWebGroovyFiles();
        try {

            /**
             * 先实例化groovy对象
             */
            for (String file : groovyCode) {
                groovyObjectLoader.instanceObject(true, app, new File(file));
            }
        } catch (Exception e) {
            AppLogger.getAppLogger(app.getAppKey(), "Groovy-Init").error(e);
            throw new DeployException("Init Error", e);
        }
        /**
         * 处理对象注入
         */
        for (String file : groovyCode) {
            try {
                groovyObjectLoader.autowiredObject(true, app, new File(file));
            } catch (Exception e) {
                AppLogger.getAppLogger(app.getAppKey(), "Groovy-Autowired").error(e);
            }
        }

    }
}
