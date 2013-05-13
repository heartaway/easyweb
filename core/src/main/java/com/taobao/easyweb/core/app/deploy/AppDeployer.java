package com.taobao.easyweb.core.app.deploy;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.change.AppChangeHolder;
import com.taobao.easyweb.core.app.scanner.ScanResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 应用部署器
 *
 * @author jimmey
 */
@Component
public class AppDeployer {

    private static Map<DeployPhase, List<DeployListener>> listeners = new HashMap<DeployPhase, List<DeployListener>>();

    public static void addListener(DeployListener deployListener) {
        Deployer deployer = deployListener.getClass().getAnnotation(Deployer.class);
        DeployPhase phase = DeployPhase.COMPILE_GROOVY;
        if (deployer != null) {
            phase = deployer.value();
        }
        List<DeployListener> list = listeners.get(phase);
        if (list == null) {
            list = new LinkedList<DeployListener>();
            listeners.put(phase, list);
        }
        list.add(deployListener);
    }

    public void deploy(App app, ScanResult result) throws DeployException {
        //先停止app服务
        AppChangeHolder.stop(app);

        //checkout代码

        //调用初始化处理
        for (DeployPhase phase : DeployPhase.getAll()) {
            List<DeployListener> list = listeners.get(phase);
            if (list == null) {
                continue;
            }
            for (DeployListener processor : list) {
                processor.process(result);
            }
        }

        //app提供服务
        AppChangeHolder.start(app);
    }

}
