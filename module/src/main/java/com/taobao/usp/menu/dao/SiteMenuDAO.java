package com.taobao.usp.menu.dao;

import com.taobao.usp.domain.SiteMenu;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jimmey
 * Date: 13-3-26
 * Time: ÏÂÎç7:19
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SiteMenuDAO extends SqlMapClientDaoSupport {


    public List<SiteMenu> querySiteMenus(Integer siteCategory) {
        return getSqlMapClientTemplate().queryForList("siteMenuDAO.querySiteMenus", siteCategory);
    }

    public List<SiteMenu> query(SiteMenu siteMenu) {
        return getSqlMapClientTemplate().queryForList("siteMenuDAO.query", siteMenu);
    }

    public void insert(SiteMenu siteMenu) {
        getSqlMapClientTemplate().insert("siteMenuDAO.insert", siteMenu);
    }

    public void update(SiteMenu siteMenu) {
        getSqlMapClientTemplate().update("siteMenuDAO.update", siteMenu);
    }

    public void delete(SiteMenu siteMenu) {
        getSqlMapClientTemplate().delete("siteMenuDAO.delete", siteMenu);
    }

    public SiteMenu findById(Integer id) {
        return (SiteMenu) getSqlMapClientTemplate().queryForObject("siteMenuDAO.queryById", id);
    }

    public List<SiteMenu> queryChildren(Integer siteCategory, Integer parentId) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("parentId", parentId);
        map.put("siteCategory", siteCategory);
        return getSqlMapClientTemplate().queryForList("siteMenuDAO.queryChildren", map);
    }

    public List<SiteMenu> querySiteMenu(SiteMenu siteMenu) {
        return getSqlMapClientTemplate().queryForList("siteMenuDAO.querySiteMenu", siteMenu);
    }

    public List<Integer> queryMenuApps(Integer siteCategory) {
        return getSqlMapClientTemplate().queryForList("siteMenuDAO.queryMenuApps", siteCategory);
    }

    public void deleteSiteAppMenus(Integer siteCategory, Integer appId) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("siteCategory", siteCategory);
        map.put("appId", appId);
        getSqlMapClientTemplate().delete("siteMenuDAO.deleteSiteAppMenus", map);
    }

}
