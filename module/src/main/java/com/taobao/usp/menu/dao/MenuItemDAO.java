package com.taobao.usp.menu.dao;

import com.taobao.usp.domain.MenuItem;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jimmey
 * Date: 13-3-26
 * Time: 下午7:19
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MenuItemDAO extends SqlMapClientDaoSupport {


    public List<MenuItem> queryAll() {
        return getSqlMapClientTemplate().queryForList("menuItemDAO.queryAll");
    }

    public void insert(MenuItem menuItem) {
        getSqlMapClientTemplate().insert("menuItemDAO.insert", menuItem);
    }

    public void update(MenuItem menuItem) {
        getSqlMapClientTemplate().update("menuItemDAO.update", menuItem);
    }

    public void delete(Integer id) {
        getSqlMapClientTemplate().update("menuItemDAO.delete", id);
    }

    public MenuItem queryById(Integer id) {
        return (MenuItem) getSqlMapClientTemplate().queryForObject("menuItemDAO.queryById", id);
    }

    /**
     * 查询出module的父菜单对象
     *
     * @param moduleId
     * @return
     */
    public List<MenuItem> queryModuleTopMenus(Integer moduleId) {
        return getSqlMapClientTemplate().queryForList("menuItemDAO.queryModuleTopMenus", moduleId);
    }

    public List<MenuItem> queryModuleMenus(Integer moduleId) {
        return getSqlMapClientTemplate().queryForList("menuItemDAO.queryModuleMenus", moduleId);
    }

    public List<MenuItem> queryChildren(Integer parentId) {
        return getSqlMapClientTemplate().queryForList("menuItemDAO.queryChildren", parentId);
    }

}
