package com.taobao.usp.module;

import com.taobao.usp.domain.SiteModule;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-2 ÏÂÎç9:48
 */
@Component
public class SiteModuleDAO extends SqlMapClientDaoSupport {

    public void insert(SiteModule siteModule){
        getSqlMapClientTemplate().insert("siteModuleDAO.insert",siteModule);
    }

    public void delete(SiteModule siteModule){
        getSqlMapClientTemplate().delete("siteModuleDAO.delete",siteModule);
    }

    public List<SiteModule> query(SiteModule siteModule){
        return getSqlMapClientTemplate().queryForList("siteModuleDAO.query",siteModule);
    }

    public List<SiteModule> querySiteModules(Integer siteCategory){
        return getSqlMapClientTemplate().queryForList("siteModuleDAO.querySiteModules",siteCategory);
    }

}
