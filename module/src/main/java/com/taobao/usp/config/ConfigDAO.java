package com.taobao.usp.config;

import com.taobao.usp.domain.ConfigInfo;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 ÏÂÎç9:42
 */
@Component
public class ConfigDAO extends SqlMapClientDaoSupport {

    public void insert(ConfigInfo config) {
        getSqlMapClientTemplate().insert("configDAO.insert", config);
    }

    public void update(ConfigInfo config) {
        getSqlMapClientTemplate().update("configDAO.update", config);
    }

    public List<ConfigInfo> queryModuleConfigs(Integer moduleId) {
        return getSqlMapClientTemplate().queryForList("configDAO.queryModuleConfigs", moduleId);
    }

    public ConfigInfo queryByKey(String id) {
        return (ConfigInfo) getSqlMapClientTemplate().queryForObject("configDAO.queryByKey", id);
    }

    public List<ConfigInfo> query(ConfigInfo config) {
        return getSqlMapClientTemplate().queryForList("configDAO.query", config);
    }

    public List<String> queryModuleConfigGroups(Integer moduleId) {
        return getSqlMapClientTemplate().queryForList("configDAO.queryModuleConfigGroups", moduleId);
    }

}
