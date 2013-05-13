package com.taobao.easyweb.core.app.deploy;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 下午10:13
 */
public enum DeployPhase {
    PARSE_CONFIG(com.taobao.easyweb.core.app.deploy.Phase.PARSE_CONFIG),//环境初始化
    COMPILE_GROOVY(Phase.COMPILE_GROOVY),//停止服务（如果已有app在运行）
    INIT_GROOVYOBJECT(Phase.INIT_GROOVYOBJECT),//实例化groovy对象
    AFTER_INIT(Phase.AFTER_INIT);//groovy对象实例化之后
    int phase;

    private DeployPhase(int phase) {
        this.phase = phase;
    }

    public static DeployPhase[] getAll() {
        return new DeployPhase[]{PARSE_CONFIG, COMPILE_GROOVY, INIT_GROOVYOBJECT, AFTER_INIT};
    }
}
