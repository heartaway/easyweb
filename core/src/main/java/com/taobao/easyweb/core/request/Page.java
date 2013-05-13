package com.taobao.easyweb.core.request;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface Page {
    String url() default "";

    String title() default "";// “≥√Ê±ÍÃ‚

    String layout() default "";

    String method() default "get";

}
