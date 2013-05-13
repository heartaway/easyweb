package com.taobao.usp.module;

import com.taobao.usp.domain.AppInfo;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 ÏÂÎç9:28
 */
@Component("uspAppInfoDAO")
public class AppInfoDAO extends SqlMapClientDaoSupport {

    public void insert(AppInfo appInfo) {
        super.getSqlMapClientTemplate().insert("moduleDAO.insert", appInfo);
    }

    public void delete(Integer id){
        getSqlMapClientTemplate().delete("moduleDAO.delete",id);
    }

    public void update(AppInfo appInfo){
        getSqlMapClientTemplate().update("moduleDAO.update", appInfo);
    }

    public List<AppInfo> queryByScope(Integer scope) {
        return getSqlMapClientTemplate().queryForList("moduleDAO.queryByScope", scope);
    }

    public List<AppInfo> queryAll() {
        return getSqlMapClientTemplate().queryForList("moduleDAO.queryAll");
    }

    public AppInfo queryById(Integer id) {
        return (AppInfo) getSqlMapClientTemplate().queryForObject("moduleDAO.queryById", id);
    }


}
