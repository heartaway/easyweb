package com.taobao.easyweb.orm.nutz;

import java.lang.annotation.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 ����2:07
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NutzDAO {

    String name() default "";

    String datasource();

}
