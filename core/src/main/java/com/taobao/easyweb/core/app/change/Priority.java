package com.taobao.easyweb.core.app.change;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 ����9:55
 */
public enum Priority {
    LOW, //�����ȼ������ִ�С���classloader��ж��
    DEFAULT, //Ĭ�����ȼ����ڸ����ȼ�֮��
    HIGH;//�����ȼ�������ִ��

    public static Priority[] getAll() {
        return new Priority[]{HIGH, DEFAULT, LOW};
    }
}
