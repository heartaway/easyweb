package com.taobao.easyweb.orm.datasource;

import com.taobao.easyweb.core.app.App;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 ÏÂÎç2:15
 */
public class DatasourceFactory {

    private static Map<String, DataSource> appDatasource = new ConcurrentHashMap<String, DataSource>(2);

    public static void regist(App app, String datasourceName, DataSource dataSource) {
        appDatasource.put(getAppDs(app, datasourceName), dataSource);
    }

    public static DataSource getDatasouce(App app, String name) {
        return appDatasource.get(getAppDs(app, name));
    }

    private static String getAppDs(App app, String name) {
        return app.getAppKey() + "-" + name;
    }


}
