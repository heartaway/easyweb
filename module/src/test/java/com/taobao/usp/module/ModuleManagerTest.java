package com.taobao.usp.module;

import com.taobao.usp.BaseTest;
import com.taobao.usp.domain.AppInfo;
import org.junit.Assert;
import org.junit.Test;
import org.unitils.spring.annotation.SpringBean;

import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 ÏÂÎç10:56
 */
public class ModuleManagerTest extends BaseTest {

    @SpringBean("moduleManager")
    private AppInfoManager appInfoManager;

    @Test
    public void testInsert() {
        AppInfo appInfo = new AppInfo();
        appInfo.setTitle("×°ÐÞ");
        appInfo.setScope(1);
        appInfo.setOwner("shantong");
        appInfoManager.registModule(appInfo);
    }

    @Test
    public void testQueryByScope() {
        List<AppInfo> l = appInfoManager.queryByScope(1);
        Assert.assertNotNull(l);
    }
}
