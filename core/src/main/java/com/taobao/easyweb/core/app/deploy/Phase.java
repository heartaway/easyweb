package com.taobao.easyweb.core.app.deploy;


public class Phase {

    /**
     * ��Ҫ�����ʼ��֮ǰ���õģ�������Դ��ʼ��
     */
    public static final int PARSE_CONFIG = 1;
    /**
     * ����groovy
     */
    public static final int COMPILE_GROOVY = 2;
    /**
     * ʵ����groovy����
     */
    public static final int INIT_GROOVYOBJECT = 3;

    public static final int AFTER_INIT = 4;

}
