package com.taobao.easyweb.core.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: wenming.wwc
 * Date: 13-1-15
 * Time: ����8:39
 * ��װrequestΪ�����bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestBean {

}
