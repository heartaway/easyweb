package com.taobao.easyweb.core.request.pipeline;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.app.deploy.DeployException;
import com.taobao.easyweb.core.app.deploy.DeployPhase;
import com.taobao.easyweb.core.app.deploy.Deployer;
import com.taobao.easyweb.core.app.deploy.process.FileProcessor;
import com.taobao.easyweb.core.app.scanner.ScanResult;
import com.taobao.easyweb.core.bean.BeanFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 ÏÂÎç12:01
 */
@Component
@Deployer(DeployPhase.AFTER_INIT)
public class PipelineProcessor extends FileProcessor {
    @Override
    public void process(ScanResult result) throws DeployException {
        List<String> list = result.getSuffixFiles(".properties");
        if (list.isEmpty()) {
            return;
        }
        App app = result.getApp();
        for (String file : list) {
            if (!file.endsWith("pipeline.properties")) {
                continue;
            }
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(file));
                String pipelines = properties.getProperty("pipeline.valves");
                if (StringUtils.isBlank(pipelines)) {
                    continue;
                }
                List<Valve> valves = new LinkedList<Valve>();
                String[] v = pipelines.split(",");
                for (String name : v) {
                    Object obj = BeanFactory.getAppBean(app.getAppKey(), name);
                    if (obj == null || !(obj instanceof Valve)) {
                        AppLogger.getAppLogger(app.getAppKey(), "PipelineProcessor init error:");
                        throw new DeployException("pipeline " + name + " error");
                    }
                    valves.add((Valve) obj);
                }
                Pipeline.initPipeline(result.getApp(), valves);
            } catch (IOException e) {
            }
        }
    }
}
