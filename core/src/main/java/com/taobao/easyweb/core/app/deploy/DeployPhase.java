package com.taobao.easyweb.core.app.deploy;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 ����10:13
 */
public enum DeployPhase {
    PARSE_CONFIG(com.taobao.easyweb.core.app.deploy.Phase.PARSE_CONFIG),//������ʼ��
    COMPILE_GROOVY(Phase.COMPILE_GROOVY),//ֹͣ�����������app�����У�
    INIT_GROOVYOBJECT(Phase.INIT_GROOVYOBJECT),//ʵ����groovy����
    AFTER_INIT(Phase.AFTER_INIT);//groovy����ʵ����֮��
    int phase;

    private DeployPhase(int phase) {
        this.phase = phase;
    }

    public static DeployPhase[] getAll() {
        return new DeployPhase[]{PARSE_CONFIG, COMPILE_GROOVY, INIT_GROOVYOBJECT, AFTER_INIT};
    }
}
