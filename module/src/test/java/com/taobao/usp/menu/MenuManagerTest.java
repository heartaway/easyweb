package com.taobao.usp.menu;

import com.taobao.usp.BaseTest;
import com.taobao.usp.domain.MenuItem;
import com.taobao.usp.domain.SiteMenu;
import org.junit.Assert;
import org.junit.Test;
import org.unitils.spring.annotation.SpringBean;

import java.util.List;

public class MenuManagerTest extends BaseTest {

    @SpringBean("menuManager")
    private MenuManager menuManager;

    @Test
    public void testInsertMenu(){
        MenuItem menuItem = new MenuItem();
        menuItem.setUri("/backyard/index.html");
        menuItem.setTitle("µÍ∆Ã∫Û‘∫");
        menuItem.setParentId(0);
        menuItem.setId(1);
        menuManager.registMenuItem(menuItem);
    }

    @Test
    public void insertSiteMenu() {
        SiteMenu siteMenu = new SiteMenu();
        siteMenu.setMenuItemId(1);
        siteMenu.setParentId(0);
        siteMenu.setSiteCategory(1);
        siteMenu.setTitle("test");
        menuManager.insertSiteMenu(siteMenu);
    }

    @Test
    public void querySiteMenus(){
        List<SiteMenu> l = menuManager.querySiteMenus(1);
        Assert.assertNotNull(l);
    }
}
