package com.taobao.usp.menu;

import com.taobao.usp.config.ConfigManager;
import com.taobao.usp.domain.AppInfo;
import com.taobao.usp.domain.MenuItem;
import com.taobao.usp.domain.SiteConfig;
import com.taobao.usp.domain.SiteMenu;
import com.taobao.usp.menu.dao.MenuItemDAO;
import com.taobao.usp.menu.dao.SiteMenuDAO;
import com.taobao.usp.module.AppInfoManager;
import groovy.json.JsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jimmey
 * Date: 13-3-26
 * Time: 下午9:15
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MenuManager {

    @Resource(name = "siteMenuDAO")
    private SiteMenuDAO siteMenuDAO;
    @Resource(name = "menuItemDAO")
    private MenuItemDAO menuItemDAO;
    @Resource
    private ConfigManager configManager;
    @Resource
    private AppInfoManager appInfoManager;

    public void registMenuItem(MenuItem menuItem) {
        if (menuItem.getId() != null) {
            menuItemDAO.update(menuItem);
        } else {
            menuItemDAO.insert(menuItem);
        }
    }

    public Integer insertSiteMenu(SiteMenu siteMenu) {
        List<SiteMenu> list = siteMenuDAO.querySiteMenu(siteMenu);
        if (list.isEmpty()) {
            siteMenuDAO.insert(siteMenu);
            list = siteMenuDAO.querySiteMenu(siteMenu);
        }
        return list.isEmpty() ? 0 : list.get(0).getId();
    }

    public void delete(Integer id) {
        menuItemDAO.delete(id);
    }

//    public List<SiteMenu> querySiteMenu(Integer siteCategory) {
//        return siteMenuDAO.querySiteMenus(siteCategory);
//    }

    public List<MenuItem> queryAll() {
        return menuItemDAO.queryAll();
    }

    public MenuItem queryMenuItemById(Integer id) {
        return menuItemDAO.queryById(id);
    }

    public SiteMenu querySiteMenuById(Integer id) {
        return siteMenuDAO.findById(id);
    }

    public void deleteSiteMenu(Integer siteCategory, Integer menuItemid) {
        SiteMenu siteMenu = new SiteMenu();
        siteMenu.setSiteCategory(siteCategory);
        siteMenu.setMenuItemId(menuItemid);
        siteMenuDAO.delete(siteMenu);
    }

    public List<MenuItem> queryModuleMenus(Integer moduleId) {
        return menuItemDAO.queryModuleMenus(moduleId);
    }

    public List<MenuItem> queryModuleTopMenus(Integer moduleId) {
        return menuItemDAO.queryModuleTopMenus(moduleId);
    }

    public List<MenuItem> queryMenuChildren(Integer parentId) {
        return menuItemDAO.queryChildren(parentId);
    }

    public List<SiteMenu> querySiteMenus(Integer siteCategory) {
        List<SiteMenu> siteMenus = siteMenuDAO.queryChildren(siteCategory, 0);
        for (SiteMenu siteMenu : siteMenus) {
            setSiteMenuChildren(siteMenu);
        }
        return siteMenus;
    }

    /**
     * 递归设置子菜单
     *
     * @param siteMenu
     */
    private void setSiteMenuChildren(SiteMenu siteMenu) {
        List<SiteMenu> siteMenus = siteMenuDAO.queryChildren(siteMenu.getSiteCategory(), siteMenu.getId());
        MenuItem menuItem = menuItemDAO.queryById(siteMenu.getMenuItemId());
        if (menuItem == null) {
            return;
        }
        siteMenu.setMenuItem(menuItem);
        if (siteMenus.isEmpty()) {
            return;
        }
        siteMenu.setChildren(siteMenus);
        for (SiteMenu child : siteMenus) {
            setSiteMenuChildren(child);
        }
    }

    public void syncMenus(Integer siteCategory) {
        List<Integer> list = siteMenuDAO.queryMenuApps(siteCategory);
        List<AppInfo> apps = appInfoManager.querySiteModules(siteCategory);
        for (AppInfo appInfo : apps) {
            list.remove(appInfo.getId());
            List<MenuItem> items = menuItemDAO.queryModuleMenus(appInfo.getId());
            for (MenuItem menuItem : items) {
                initMenu(menuItem, siteCategory);
            }
        }
        if (!list.isEmpty()) {//还有，则删除
            for (Integer app : list) {
                siteMenuDAO.deleteSiteAppMenus(siteCategory, app);
            }
        }
    }

    private void initMenu(MenuItem menuItem, Integer siteCategory) {
        SiteMenu p = null;
        if (menuItem.getParentId() != null && menuItem.getParentId() != 0) {
            MenuItem parent = menuItemDAO.queryById(menuItem.getParentId());
            initMenu(parent, siteCategory);

            SiteMenu query = new SiteMenu();
            query.setSiteCategory(siteCategory);
            query.setMenuItemId(parent.getId());
            List<SiteMenu> l = siteMenuDAO.querySiteMenu(query);
            if (!l.isEmpty()) {
                p = l.get(0);
            }
        }

        int parentId = p == null ? 0 : p.getId();
        SiteMenu siteMenu = new SiteMenu();
        siteMenu.setSiteCategory(siteCategory);
        siteMenu.setMenuItemId(menuItem.getId());
        siteMenu.setStatus(0);
        siteMenu.setTitle(menuItem.getTitle());
        siteMenu.setAppId(menuItem.getModuleId());
        siteMenu.setParentId(parentId);
        insertSiteMenu(siteMenu);
    }

    public List<SiteMenu> querySiteAppMenus(Integer appId, Integer siteCategory) {
        SiteMenu query = new SiteMenu();
        query.setSiteCategory(siteCategory);
        query.setAppId(appId);
        List<SiteMenu> list = siteMenuDAO.query(query);
        Map<Integer, SiteMenu> map = new HashMap<Integer, SiteMenu>();
        for (SiteMenu siteMenu : list) {
            map.put(siteMenu.getMenuItemId(), siteMenu);
        }
        List<MenuItem> appMenus = queryModuleMenus(appId);

        for (MenuItem menuItem : appMenus) {
            if (!map.containsKey(menuItem.getId())) {
                SiteMenu siteMenu = new SiteMenu();
                siteMenu.setAppId(appId);
                siteMenu.setSiteCategory(siteCategory);
                siteMenu.setParentId(menuItem.getParentId());
                siteMenu.setTitle(menuItem.getTitle());
                siteMenu.setStatus(0);
                siteMenu.setMenuItemId(menuItem.getId());
                insertSiteMenu(siteMenu);
            }
        }
        return siteMenuDAO.query(query);
    }

    public void syncSiteMenu(Integer siteCategory) {
        //先获取配置好的菜单
        Menu menu = getSiteMenus(siteCategory);
        String content = new JsonBuilder(menu).toString();

        //更新配置到数据库
        SiteConfig siteConfig = new SiteConfig();
        siteConfig.setSiteCategory(siteCategory);
        siteConfig.setKey("site.navMenu");
        siteConfig.setModuleId(0);
        siteConfig.setValue(content);
        configManager.setSiteConfig(siteConfig);

        //发布站点配置到diamond
        configManager.publishSiteConfig(siteCategory);
    }

    public Menu getSiteMenus(Integer siteCategory) {
        Menu menu = new Menu();
        menu.setTitle("root");//父节点
        List<SiteMenu> siteMenus = siteMenuDAO.queryChildren(siteCategory, 0);
        for (SiteMenu siteMenu : siteMenus) {
            Menu child = buildMenu(siteMenu, siteCategory);
            if (child != null) {
                menu.addChild(child);
            }
        }
        return menu;
    }

    /**
     * 构建子菜单
     *
     * @param siteMenu
     * @param siteCategory
     * @return
     */
    private Menu buildMenu(SiteMenu siteMenu, Integer siteCategory) {
        MenuItem menuItem = menuItemDAO.queryById(siteMenu.getMenuItemId());
        if (menuItem == null) {
            return null;
        }

        Menu menu = new Menu();
        menu.setTitle(StringUtils.isBlank(siteMenu.getTitle()) ? menuItem.getTitle() : siteMenu.getTitle());
        menu.setParams(menuItem.getParam());
        menu.setUri(menuItem.getUri());
        menu.setGroovyCode(menuItem.getGroovyCode());
        //查询子菜单
        List<SiteMenu> siteMenus = siteMenuDAO.queryChildren(siteCategory, siteMenu.getId());
        for (SiteMenu sm : siteMenus) {
            Menu child = buildMenu(sm, siteCategory);
            if (child != null) {
                menu.addChild(child);
            }
        }
        return menu;
    }
}