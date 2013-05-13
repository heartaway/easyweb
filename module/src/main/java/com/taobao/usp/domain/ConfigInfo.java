package com.taobao.usp.domain;

import java.util.Date;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-1 ����9:36
 * <p/>
 * һ��ģ���ڶ�Ӧվ���µ�����
 */
public class ConfigInfo {

    /**
     * ���ݿ��ֶ�id
     */
    private Integer id;
    /**
     * ����ģ��id���������ú�̨����ע���Ĺ���app���ݿ�id��ͨ����ѡ�ķ�ʽ��ȡ��
     * ��װ�޹��ܵ�moduleId=2
     */
    private Integer moduleId;
    /**
     * �����ڹ����µķ��飬��װ�޹��������Թ��ڡ���ʽ�������ã���ô��ʽ����һ������
     */
    private String group;
    /**
     * �����õ�һ��������Ϣ
     */
    private String description;
    /**
     * ���õ�key����ð�װͳһ��ǰ׺��ʽ��Ҫ����ȫ��Ψһ�ģ��������ظ���ֱ�������ݿ�Ψһ��֤
     */
    private String key;
    /**
     * ���õ�Ĭ��ֵ
     */
    private String defaultValue;
    /**
     * ֵ���ͣ����Բ�����
     * 1��string
     * 2��boolean
     * 3��int
     * 4��long
     * 5��Object
     */
    private String valueType;
    /**
     * ��������ͣ��������������ÿ�ʹ��
     * 1��input
     * 2��radio
     * 3��select
     * 4��checkbox
     * 5��textarea
     * 6��file
     * 7��code��ʹ��ace���༭����
     */
    private Integer inputType;
    /**
     * ��������͵����ã���
     * radio��ѡ��ֵ����/����ȡ/���� ֵ��������Ӣ��ð�ŷָ�����Ӣ�ķֺŷָ�
     * select��ÿ��ѡ�� ֵ��������Ӣ��ð�ŷָ�����Ӣ�ķֺŷָ�
     * checkbox�ļ����ɹ�ѡ���ֵ��������Ӣ��ð�ŷָ�����Ӣ�ķֺŷָ�
     * code������
     */
    private String inputTypeConfig;

    /**
     * 0:Ĭ������
     * 1:diamond����
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
