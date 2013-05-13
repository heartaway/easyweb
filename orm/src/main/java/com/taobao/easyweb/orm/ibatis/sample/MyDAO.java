package com.taobao.easyweb.orm.ibatis.sample;

import com.taobao.easyweb.orm.ibatis.annotation.IbatisDAO;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-28 обнГ8:00
 */
@IbatisDAO(name="myDA",datasource = "testsc")
public interface MyDAO {

    Object insert();

}
