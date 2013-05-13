package com.taobao.easyweb.core.request;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParser;
import groovy.lang.GroovyObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class PageAnnotationParser extends AnnotationParser {

    @Override
    public boolean isParse(Annotation annotation) {
        return annotation instanceof Page;
    }

    @Override
    public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
        if (!(target instanceof Method)) {
            return;
        }
        Method javaMethod = (Method) target;
        Page page = javaMethod.getAnnotation(Page.class);
        if (page == null) {// 只支持没有参数的方法
            return;
        }
        PageMethod pageMethod = new PageMethod();
        pageMethod.setUrl(page.method() + ":" + page.url());
        pageMethod.setFile(file);
        pageMethod.setMethod(javaMethod.getName());
        if (StringUtils.isNotBlank(page.layout())) {
            pageMethod.setLayout(page.layout());
        }
        RequestMapping.addPageMethod(app.getAppKey(), pageMethod);
    }

}
