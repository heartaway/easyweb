package com.taobao.usp.module;

import com.taobao.usp.domain.AppInfo;
import com.taobao.usp.domain.MenuItem;
import com.taobao.usp.domain.SiteMenu;
import com.taobao.usp.domain.SiteModule;
import com.taobao.usp.menu.MenuManager;
import com.taobao.usp.menu.dao.MenuItemDAO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 ����9:43
 */

@Component
public class AppInfoManager {

    @Resource
    private MenuItemDAO menuItemDAO;
    @Resource
    private MenuManager menuManager;
    @Resource(name = "uspAppInfoDAO")
    private AppInfoDAO appInfoDAO;
    @Resource
    private SiteModuleDAO siteModuleDAO;

    /**
     * ע��һ��ģ��
     *
     * @param appInfo
     */
    public void registModule(AppInfo appInfo) {
        if (appInfo.getId() != null) {
            appInfoDAO.update(appInfo);
        } else {
            appInfoDAO.insert(appInfo);
        }

    }

    public void delete(Integer id) {
        appInfoDAO.delete(id);
    }

    public AppInfo queryById(Integer id) {
        if (id == null) {
            return null;
        }
        return appInfoDAO.queryById(id);
    }

    /**
     * ��ѯĳ��������������Թ���ģ��
     *
     * @param scope
     * @return
     */
    public List<AppInfo> queryByScope(Integer scope) {
        return appInfoDAO.queryByScope(scope);
    }

    public List<AppInfo> queryAll() {
        return appInfoDAO.queryAll();
    }

    /**
     * ��ģ�����һ���˵�
     *
     * @param menuItem
     */
    public void addModuleMenu(MenuItem menuItem) {
        menuItemDAO.insert(menuItem);
    }

    public List<AppInfo> querySiteModules(Integer siteCategory) {
        List<SiteModule> list = siteModuleDAO.querySiteModules(siteCategory);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (SiteModule siteModule : list) {
            AppInfo info = appInfoDAO.queryById(siteModule.getModuleId());
            if (info != null) {
                appInfos.add(info);
            } else {
                siteModuleDAO.delete(siteModule);
            }
        }
        return appInfos;
    }

    public void addSiteModule(Integer siteCategory, Integer moduleId) {
        SiteModule siteModule = new SiteModule();
        siteModule.setModuleId(moduleId);
        siteModule.setSiteCategory(siteCategory);

        //����Ѿ����ˣ���������
        List<SiteModule> list = siteModuleDAO.query(siteModule);
        if (!list.isEmpty()) {
            return;
        }
        //���û�У�������Ӽ�¼����copy�˵�
        siteModuleDAO.insert(siteModule);

        //�ȴ���һ���˵�
        List<MenuItem> menuItems = menuManager.queryModuleTopMenus(moduleId);
        addChildren(menuItems, siteCategory, 0);
    }

    private void addChildren(List<MenuItem> menuItems, Integer siteCategory, Integer parentId) {
        for (MenuItem menuItem : menuItems) {
            SiteMenu siteMenu = new SiteMenu();
            siteMenu.setTitle(menuItem.getTitle());
            siteMenu.setSiteCategory(siteCategory);
            siteMenu.setParentId(parentId);
            siteMenu.setMenuItemId(menuItem.getId());
            Integer pid = menuManager.insertSiteMenu(siteMenu);
            if (pid > 0) {
                List<MenuItem> ms = menuManager.queryMenuChildren(menuItem.getId());
                addChildren(ms, siteCategory, pid);
            }
        }
    }

    /**
     * ɾ��ģ���ͬʱɾ���˵�
     *
     * @param siteCategory
     * @param moduleId
     */
    public void deleteSiteModule(Integer siteCategory, Integer moduleId) {
        SiteModule siteModule = new SiteModule();
        siteModule.setModuleId(moduleId);
        siteModule.setSiteCategory(siteCategory);

        //����Ѿ����ˣ���������
        List<SiteModule> list = siteModuleDAO.query(siteModule);
        if (!list.isEmpty()) {
            siteModuleDAO.delete(list.get(0));
        }
        List<MenuItem> menuItems = menuManager.queryModuleMenus(moduleId);
        for (MenuItem menuItem : menuItems) {
            menuManager.deleteSiteMenu(siteCategory, menuItem.getId());
        }
    }


}
