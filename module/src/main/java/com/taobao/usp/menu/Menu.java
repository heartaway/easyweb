package com.taobao.usp.menu;

import java.util.*;

/**
 * User: jimmey
 * Date: 13-3-26
 * Time: ����9:23
 *
 * ���ճ�ȥ�Ĳ˵����ݽṹ���м����ù������̲�����������
 *
 */
public class Menu {

    private String title;

    private String uri;

    private String params;

    private List<Menu> children;

    private String groovyCode;

    public Menu() {
    }

    public Menu(String title, String uri) {
        this.title = title;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public void addChild(Menu menu){
        if(this.children == null){
            this.children = new LinkedList<Menu>();
        }
        this.children.add(menu);
    }

    public String getGroovyCode() {
        return groovyCode;
    }

    public void setGroovyCode(String groovyCode) {
        this.groovyCode = groovyCode;
    }
}
