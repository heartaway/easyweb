package com.taobao.easyweb.orm.ibatis.annotation;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-28 下午7:57
 *
 * 声明是ibatis的实现
 *
 */
public @interface IbatisDAO {

    String name() default "";

    String datasource();

}
