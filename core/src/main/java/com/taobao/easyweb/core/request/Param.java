package com.taobao.easyweb.core.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface Param {
	/**
	 * ���ڱ�ʶparam�����ơ�
	 * <p>
	 * �˲������ڼ򻯵���ʽ��<code>@Param("paramName")</code>��
	 * </p>
	 */
	String value() default "";

	/**
	 * ���ڱ�ʶparam�����ơ�
	 * <p>
	 * �˲��������ж����������ʽ��<code>@Param(name="paramName", defaultValue="123")</code>��
	 * </p>
	 */
	String name() default "";

	/**
	 * ָ��������Ĭ��ֵ��
	 */
	String defaultValue() default "";

	/**
	 * ָ��������Ĭ��ֵ���顣
	 */
	String[] defaultValues() default {};

}
