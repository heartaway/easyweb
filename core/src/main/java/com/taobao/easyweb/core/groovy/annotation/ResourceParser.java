package com.taobao.easyweb.core.groovy.annotation;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.bean.BeanFactory;
import groovy.lang.GroovyObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-27 ÏÂÎç5:52
 */
@Component
public class ResourceParser extends AnnotationParser {

    public ResourceParser() {
        super(new ParsePhase[]{ParsePhase.Ioc});
    }

    @Override
    public boolean isParse(Annotation annotation) {
        return annotation instanceof Resource;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
        if (!(target instanceof Field)) {
            return;
        }

        Field field = (Field) target;
        Resource resource = (Resource) annotation;
        String name = field.getName();
        if (StringUtils.isNotBlank(resource.name())) {
            name = resource.name();
        }
        Object obj = BeanFactory.getBeans(app).get(name);
        if (obj == null) {
            try {
                obj = BeanFactory.getSpringBean(name, field.getType());
            } catch (Exception e) {
            }
        }
        if (obj != null) {
            try {
                field.setAccessible(true);
                field.set(groovyObject, obj);
            } catch (Exception e) {
            }
            groovyObject.setProperty(field.getName(), obj);
        }
    }
}
