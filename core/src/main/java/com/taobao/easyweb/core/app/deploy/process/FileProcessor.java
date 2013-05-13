package com.taobao.easyweb.core.app.deploy.process;

import com.taobao.easyweb.core.app.deploy.AppDeployer;
import com.taobao.easyweb.core.app.deploy.DeployListener;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 обнГ2:51
 */
public abstract class FileProcessor implements DeployListener {

    protected FileProcessor() {
        AppDeployer.addListener(this);
    }

}
