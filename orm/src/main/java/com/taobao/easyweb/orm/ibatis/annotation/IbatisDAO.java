package com.taobao.easyweb.orm.ibatis.annotation;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-28 ����7:57
 *
 * ������ibatis��ʵ��
 *
 */
public @interface IbatisDAO {

    String name() default "";

    String datasource();

}
