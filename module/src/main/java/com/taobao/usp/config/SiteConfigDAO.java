package com.taobao.usp.config;

import com.taobao.usp.domain.SiteConfig;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-3 ÉÏÎç10:08
 */
@Component
public class SiteConfigDAO extends SqlMapClientDaoSupport {

    public void insert(SiteConfig siteConfig) {
        getSqlMapClientTemplate().insert("siteConfigDAO.insert", siteConfig);
    }

    public void update(SiteConfig siteConfig) {
        getSqlMapClientTemplate().update("siteConfigDAO.update", siteConfig);
    }

    public SiteConfig queryById(Integer id) {
        if (id == null) {
            return null;
        }
        return (SiteConfig) getSqlMapClientTemplate().queryForObject("siteConfigDAO.queryById", id);
    }

    public List<SiteConfig> query(SiteConfig siteConfig) {
        return getSqlMapClientTemplate().queryForList("siteConfigDAO.query", siteConfig);
    }

    public List<Integer> queryConfigSites() {
        return getSqlMapClientTemplate().queryForList("siteConfigDAO.queryConfigSites");
    }

    public void delete(Integer id) {
        if (id != null) {
            getSqlMapClientTemplate().delete("siteConfigDAO.delete", id);
        }
    }

}
