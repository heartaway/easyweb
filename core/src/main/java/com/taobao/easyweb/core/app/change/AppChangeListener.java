package com.taobao.easyweb.core.app.change;

import com.taobao.easyweb.core.app.App;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 下午9:39
 * <p/>
 * 以app为维度的缓存清理，在
 */
public interface AppChangeListener {

    /**
     * 监听器初始化方法。
     */
    public void init();

    /**
     * 停止一个应用时
     *
     * @param app
     */
    public void stop(App app);

    /**
     * 删除一个应用，标示应用完全下线
     *
     * @param app
     */
    public void remove(App app);

    /**
     * 应用启动，表示启动完成了
     *
     * @param app
     */
    public void start(App app);

    public void deployError(App app);

}
