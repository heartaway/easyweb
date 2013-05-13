package com.taobao.easyweb.core.app.deploy;

import com.taobao.easyweb.core.app.scanner.ScanResult;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 обнГ3:26
 */
public interface DeployListener {

    public void process(ScanResult result) throws DeployException;

}
