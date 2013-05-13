package com.taobao.usp.domain;

import java.util.Date;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 下午9:36
 * <p/>
 * 一个模块在对应站点下的配置
 */
public class ConfigInfo {

    /**
     * 数据库字段id
     */
    private Integer id;
    /**
     * 功能模块id，是在配置后台申请注册后的功能app数据库id，通过勾选的方式获取。
     * 如装修功能的moduleId=2
     */
    private Integer moduleId;
    /**
     * 配置在功能下的分组，如装修功能下所以关于”样式“的配置，那么样式就是一个分组
     */
    private String group;
    /**
     * 对配置的一个描述信息
     */
    private String description;
    /**
     * 配置的key，最好安装统一的前缀方式。要求是全局唯一的，不可以重复。直接做数据库唯一验证
     */
    private String key;
    /**
     * 配置的默认值
     */
    private String defaultValue;
    /**
     * 值类型，可以不用了
     * 1、string
     * 2、boolean
     * 3、int
     * 4、long
     * 5、Object
     */
    private String valueType;
    /**
     * 输入框类型，用来在生成配置框使用
     * 1、input
     * 2、radio
     * 3、select
     * 4、checkbox
     * 5、textarea
     * 6、file
     * 7、code（使用ace做编辑器）
     */
    private Integer inputType;
    /**
     * 输入框类型的配置，如
     * radio的选项值：是/否，争取/错误 值和名称用英文冒号分割，多个用英文分号分割
     * select的每个选项 值和名称用英文冒号分割，多个用英文分号分割
     * checkbox的几个可供选择项，值和名称用英文冒号分割，多个用英文分号分割
     * code的类型
     */
    private String inputTypeConfig;

    /**
     * 0:默认配置
     * 1:diamond配置
     */
    private Integer type;

    private Date gmtCreate;
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Integer getInputType() {
        return inputType;
    }

    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInputTypeConfig() {
        return inputTypeConfig;
    }

    public void setInputTypeConfig(String inputTypeConfig) {
        this.inputTypeConfig = inputTypeConfig;
    }
}
