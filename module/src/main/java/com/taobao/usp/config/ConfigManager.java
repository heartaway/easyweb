package com.taobao.usp.config;

import com.taobao.usp.diamond.DiamondPublisher;
import com.taobao.usp.domain.ConfigInfo;
import com.taobao.usp.domain.SiteConfig;
import com.taobao.usp.menu.Menu;
import com.taobao.usp.menu.MenuManager;
import groovy.json.JsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 下午9:42
 */
@Component
public class ConfigManager {

    private static String SITE_CONFIG_PREFIX = "com.taobao.usp.siteConfig.";
    private static String SITE_CONFIGS = "com.taobao.usp.configSite";
    @Resource
    private ConfigDAO configDAO;
    @Resource
    private SiteConfigDAO siteConfigDAO;
    @Resource
    private DiamondPublisher diamondPublisher;
    @Resource
    private MenuManager menuManager;

    public void addConfig(ConfigInfo config) {
        ConfigInfo check = queryByKey(config.getKey());
        if (check != null && (config.getModuleId() == null || config.getModuleId().intValue() != check.getModuleId())) {
            return;
        }
        if (check != null) {
            config.setId(check.getId());
            configDAO.update(config);
            return;
        }
        configDAO.insert(config);
    }

    public ConfigInfo queryByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        ConfigInfo query = new ConfigInfo();
        query.setKey(key);
        List<ConfigInfo> list = configDAO.query(query);
        return list.isEmpty() ? null : list.get(0);
    }

    public Set<String> queryModuleConfigGroups(Integer moduleId) {
        List<String> l = configDAO.queryModuleConfigGroups(moduleId);
        return new HashSet<String>(l);
    }

    public List<ConfigInfo> queryModuleConfigs(Integer moduleId) {
        return configDAO.queryModuleConfigs(moduleId);
    }

    public void initSiteConfig(Integer moduleId, Integer siteCategory) {
        List<ConfigInfo> configs = configDAO.queryModuleConfigs(moduleId);
        for (ConfigInfo config : configs) {
            SiteConfig siteConfig = new SiteConfig();
            siteConfig.setSiteCategory(siteCategory);
            siteConfig.setKey(config.getKey());
            siteConfig.setValue(config.getDefaultValue());
            siteConfig.setModuleId(config.getModuleId());
            addSiteConfig(siteConfig, true);
        }
    }

    public List<SiteConfig> querySiteModuleConfig(Integer siteCategory, Integer module) {
        List<ConfigInfo> list = configDAO.queryModuleConfigs(module);
        Map<String, ConfigInfo> map = new HashMap<String, ConfigInfo>();
        for (ConfigInfo configInfo : list) {
            map.put(configInfo.getKey(), configInfo);
            checkSiteConfig(configInfo, siteCategory);
        }
        SiteConfig query = new SiteConfig();
        query.setSiteCategory(siteCategory);
        query.setModuleId(module);
        List<SiteConfig> configs = siteConfigDAO.query(query);
        for (SiteConfig config : configs) {
            config.setConfigInfo(map.get(config.getKey()));
        }
        return configs;
    }

    public SiteConfig queryById(Integer id) {
        SiteConfig siteConfig = siteConfigDAO.queryById(id);
        if (siteConfig != null) {
            siteConfig.setConfigInfo(configDAO.queryByKey(siteConfig.getKey()));
        }
        return siteConfig;
    }

    private void checkSiteConfig(ConfigInfo configInfo, Integer siteConfig) {
        SiteConfig query = new SiteConfig();
        query.setKey(configInfo.getKey());
        query.setSiteCategory(siteConfig);
        List<SiteConfig> list = siteConfigDAO.query(query);
        if (list.isEmpty()) {
            query.setValue(configInfo.getDefaultValue());
            query.setModuleId(configInfo.getModuleId());
            siteConfigDAO.insert(query);
        }
    }

    public List<SiteConfig> querySiteConfigs(Integer siteCategory) {
        SiteConfig query = new SiteConfig();
        query.setSiteCategory(siteCategory);
        return siteConfigDAO.query(query);
    }

    public void setSiteConfig(SiteConfig siteConfig) {
        addSiteConfig(siteConfig, false);
    }

    public void updateSiteConfig(SiteConfig siteConfig) {
        if (siteConfig.getId() == null) {
            return;
        }
        siteConfigDAO.update(siteConfig);
    }

    public void publishDiamond(Integer siteConfigId) {
        SiteConfig siteConfig = queryById(siteConfigId);
        if (siteConfig == null) {
            return;
        }
        Integer type = siteConfig.getConfigInfo().getType();
        if (type == null || type == 0) {
            return;
        }
        //将数据发布到diamond
        diamondPublisher.publish(siteConfig.getKey() + "-" + siteConfig.getSiteCategory(), siteConfig.getValue());
    }

    public void deleteSiteConfig(Integer id) {
        siteConfigDAO.delete(id);
    }

    /**
     * 发布所有配置数据
     */
    public void publishAll() {
        List<Integer> sites = siteConfigDAO.queryConfigSites();
        for (Integer site : sites) {
            publishSiteConfig(site);
        }
        publishAllSite(sites);
    }

    public void publishSiteConfig(Integer siteCategory) {
        setSiteMenu(siteCategory);
        SiteConfig query = new SiteConfig();
        query.setSiteCategory(siteCategory);
        List<SiteConfig> configs = siteConfigDAO.query(query);
        Map<String, String> map = new HashMap<String, String>(configs.size());
        for (SiteConfig config : configs) {
            map.put(config.getKey(), config.getValue());
        }
        JsonBuilder jsonBuilder = new JsonBuilder(map);
        String json = jsonBuilder.toString();
        diamondPublisher.publish(SITE_CONFIG_PREFIX + siteCategory, json);
        publishAllSite(null);
    }

    private void publishAllSite(List<Integer> sites) {
        if (sites == null) {
            sites = siteConfigDAO.queryConfigSites();
        }
        StringBuilder sb = new StringBuilder();
        for (Integer site : sites) {
            sb.append(SITE_CONFIG_PREFIX).append(site).append("\n");
        }
        diamondPublisher.publish(SITE_CONFIGS, sb.toString());
    }

    private void setSiteMenu(Integer siteCategory) {
        Menu menu = menuManager.getSiteMenus(siteCategory);
        SiteConfig siteConfig = new SiteConfig();
        siteConfig.setKey("com.taobao.usp.nav.v1");
        siteConfig.setModuleId(0);
        siteConfig.setSiteCategory(siteCategory);
        siteConfig.setValue(new JsonBuilder(menu).toString());
        addSiteConfig(siteConfig, false);
    }

    private void addSiteConfig(SiteConfig siteConfig, boolean init) {
        SiteConfig query = new SiteConfig();
        query.setKey(siteConfig.getKey());
        query.setSiteCategory(siteConfig.getSiteCategory());
        List<SiteConfig> list = siteConfigDAO.query(query);
        if (list.isEmpty()) {
            siteConfigDAO.insert(siteConfig);
        } else {
            SiteConfig old = list.get(0);//默认只有一个
            if ((!old.getValue().equals(siteConfig.getValue()) && !init) || (old.getValue() == null)) {
                old.setValue(siteConfig.getValue());
                siteConfigDAO.update(old);
            }
        }
    }
}
