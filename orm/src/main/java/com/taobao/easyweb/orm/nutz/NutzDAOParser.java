package com.taobao.easyweb.orm.nutz;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.bean.BeanFactory;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParser;
import com.taobao.easyweb.orm.datasource.DatasourceFactory;
import groovy.lang.GroovyObject;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 下午2:08
 */
@Component
public class NutzDAOParser extends AnnotationParser {

    @Override
    public boolean isParse(Annotation annotation) {
        return annotation instanceof NutzDAO;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
        Field[] fields = groovyObject.getClass().getDeclaredFields();
        Field dao = null;
        for (Field field : fields) {
            if (field.getType() == Dao.class) {
                dao = field;
                break;
            }
        }
        if (dao == null) {
            //没有注入dao
            return;
        }

        NutzDAO nutzDAO = (NutzDAO) annotation;
        dao.setAccessible(true);
        DataSource dataSource = DatasourceFactory.getDatasouce(app, nutzDAO.datasource());
        if (dataSource == null) {
            throw new RuntimeException("datasource "+nutzDAO.datasource()+" not prepared");
        }
        Dao impl = new NutDao(dataSource);
        try {
            dao.set(groovyObject, impl);
        } catch (IllegalAccessException e) {
        }
        BeanFactory.regist(app, nutzDAO.name(), groovyObject);
    }
}
