package com.taobao.easyweb.core.request.debug;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 ����9:26
 */
public class Info {

    /**
     * ���ݱ���
     */
    private String title;
    /**
     * ����
     */
    private String content;
    /**
     * ������ͣ�
     * 0��ֱ���ı����
     * 1��pre��ǩ���
     * 2��pre�������
     */
    private int type = 0;
    /**
     * pre�ĸ߶�
     */
    private int height = 200;

    public Info() {
    }

    public Info(String title) {
        this.title = title;
    }

    public Info(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Info(String title, String content, int type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public Info(String title, String content, int type, int height) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
