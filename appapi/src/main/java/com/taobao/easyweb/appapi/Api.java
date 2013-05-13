package com.taobao.easyweb.appapi;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {

	/**
	 * api名称
	 * @return
	 */
	String value();
	/**
	 * 是否做签名验证
	 * @return
	 */
	boolean signcheck() default true;


}
