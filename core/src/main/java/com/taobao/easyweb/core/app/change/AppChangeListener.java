package com.taobao.easyweb.core.app.change;

import com.taobao.easyweb.core.app.App;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 ����9:39
 * <p/>
 * ��appΪά�ȵĻ���������
 */
public interface AppChangeListener {

    /**
     * ��������ʼ��������
     */
    public void init();

    /**
     * ֹͣһ��Ӧ��ʱ
     *
     * @param app
     */
    public void stop(App app);

    /**
     * ɾ��һ��Ӧ�ã���ʾӦ����ȫ����
     *
     * @param app
     */
    public void remove(App app);

    /**
     * Ӧ����������ʾ���������
     *
     * @param app
     */
    public void start(App app);

    public void deployError(App app);

}
