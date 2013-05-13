package com.taobao.easyweb.orm.datasource;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.deploy.DeployException;
import com.taobao.easyweb.core.app.deploy.DeployPhase;
import com.taobao.easyweb.core.app.deploy.Deployer;
import com.taobao.easyweb.core.app.deploy.process.FileProcessor;
import com.taobao.easyweb.core.app.scanner.ScanResult;
import com.taobao.tddl.jdbc.group.TGroupDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.nutz.dao.impl.SimpleDataSource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 下午5:23
 */
@Component
@Deployer(DeployPhase.PARSE_CONFIG)
public class DataSourceProcessor extends FileProcessor {

    @Override
    public void process(ScanResult result) throws DeployException {
        List<String> list = result.getSuffixFiles(".properties");
        if (list.isEmpty()) {
            return;
        }
        App app = result.getApp();
        for (String file : list) {
            if (!file.endsWith("datasource.properties")) {
                continue;
            }
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(file));
            } catch (Exception e) {
            }

            String type = properties.getProperty("ds.type");
            String name = properties.getProperty("ds.name");
            String driverClassName = properties.getProperty("ds.driverClassName");
            String url = properties.getProperty("ds.url");
            String username = properties.getProperty("ds.username", "");
            String password = properties.getProperty("ds.password", "");

            if ("simple".equals(type)) {
                SimpleDataSource dataSource = new SimpleDataSource();
                try {
                    dataSource.setDriverClassName(driverClassName);
                } catch (ClassNotFoundException e) {
                }
                dataSource.setJdbcUrl(url);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                DatasourceFactory.regist(app, name, dataSource);
            } else if ("tddl".equals(type)) {//tddl的采用spring bean注入
                String appName = properties.getProperty("tddl.appName");
                String dbGroupKey = properties.getProperty("tddl.dbGroupKey");
                if (StringUtils.isBlank(appName) || StringUtils.isBlank(dbGroupKey)) {
                    throw new RuntimeException("tddl datasource config error");
                }
                TGroupDataSource dataSource = new TGroupDataSource();
                dataSource.setAppName(appName);
                dataSource.setDbGroupKey(dbGroupKey);
                dataSource.init();
                DatasourceFactory.regist(app, name, dataSource);
            } else if ("dbcp".equals(type)) {
                BasicDataSource basicDataSource = new BasicDataSource();
                basicDataSource.setDriverClassName(driverClassName);
                basicDataSource.setUrl(url);
                basicDataSource.setUsername(username);
                basicDataSource.setPassword(password);
                DatasourceFactory.regist(app, name, basicDataSource);
            }
        }


    }
}
