package com.taobao.easyweb.core.groovy.groovyobject;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.bean.BeanFactory;
import com.taobao.easyweb.core.groovy.BeanBinding;
import com.taobao.easyweb.core.groovy.MethodInterceptor;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParser;
import com.taobao.easyweb.core.groovy.annotation.AnnotationParserFactory;
import com.taobao.easyweb.core.request.Page;
import com.taobao.easyweb.core.velocity.GroovyVelocityEngine;
import groovy.lang.*;
import org.apache.log4j.Logger;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 上午12:07
 * <p/>
 * groovy对象加载类，和具体的classloader关联，并且classloader会和文件的类型对应
 */
@Component
public class GroovyObjectLoader {

    @Resource(name = "ewMethodInterceptor")
    private MethodInterceptor methodInterceptor;
    @Resource(name = "ewBinding")
    private BeanBinding binding;
    @Resource(name = "ewGroovyVelocityEngine")
    private GroovyVelocityEngine groovyVelocityEngine;

    /**
     * 实例化app中biz下的所有对象
     *
     * @param webClass 是否是web groovy，会根据这个参数获取web classloader还是biz classloader
     * @param app
     * @param file
     */
    public void instanceObject(boolean webClass, App app, File file) {
        GroovyClassLoader classLoader = getByType(webClass, app);
        CacheDO cacheDO = Cacher.get(classLoader, file);
        if (cacheDO == null) {
            Logger logger = AppLogger.getAppLogger(app.getAppKey(), "ClassLoader");
            try {
                Class<?> clazz = null;
                if (webClass) {//对应两种不同的load方式
                    try {
                        GroovyCodeSource codeSource = new GroovyCodeSource(file, "GBK");
                        clazz = AppClassLoaderFactory.getAppWebClassLoader(app).parseClass(codeSource, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e);
                    }
                } else {
                    clazz = classLoader.loadClass(FileMainClass.get(file.getAbsolutePath()));
                }

                GroovyObject obj = (GroovyObject) clazz.newInstance();
                parseAnnotation(AnnotationParser.ParsePhase.Init, app, file, obj);
                cacheDO = new CacheDO();
                cacheDO.setLastModified(file.lastModified());
                cacheDO.setObj(obj);
                cacheDO.setAutowired(false);
                Cacher.put(classLoader, file, cacheDO);
            } catch (ClassNotFoundException e) {
                logger.error(e);
            } catch (InstantiationException e) {
                logger.error(e);
            } catch (IllegalAccessException e) {
                logger.error(e);
            }
        }
    }

    public void autowiredObject(boolean webClass, App app, File file) {
        GroovyClassLoader classLoader = getByType(webClass, app);
        CacheDO cacheDO = Cacher.get(classLoader, file);
        if (cacheDO == null) {
            return;
        }
        GroovyObject object = cacheDO.getObj();
        binding.setProperty("vm", groovyVelocityEngine);
        binding.setProperty("log", AppLogger.getLogger("GroovyScript"));
        if (object instanceof Script) {
            Script script = (Script) object;
            script.setBinding(binding);
        } else {
            InvokerHelper.setProperties(object, binding.getVariables());
            InvokerHelper.setProperties(object, BeanFactory.getBeans(app));
        }
        if (app != null) {
            parseAnnotation(AnnotationParser.ParsePhase.Ioc, app, file, object);
        }
        MetaClassProxy proxy;
        try {
            proxy = MetaClassProxy.getMyInstance(object.getClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        proxy.setInterceptor(methodInterceptor);
        object.setMetaClass(proxy);
        cacheDO.setAutowired(true);
    }

    public GroovyObject getObject(boolean webClass, App app, File file) {
        GroovyClassLoader classLoader = getByType(webClass, app);
        CacheDO cacheDO = Cacher.get(classLoader, file);
        if (cacheDO == null) {//如果没有，先执行一下
            instanceObject(webClass, app, file);
            autowiredObject(webClass, app, file);
        } else if (cacheDO.isAutowired()) {
            autowiredObject(webClass, app, file);
        }
        cacheDO = Cacher.get(classLoader, file);
        return cacheDO.getObj();
    }

    private GroovyClassLoader getByType(boolean webClass, App app) {
        if (webClass) {
            return AppClassLoaderFactory.getAppWebClassLoader(app);
        } else {
            return AppClassLoaderFactory.getAppBizClassLoader(app);
        }
    }

    private void parseAnnotation(AnnotationParser.ParsePhase parsePhase, App app, File file, GroovyObject object) {
        if (object instanceof Script) {
            Script script = (Script) object;
            List<MetaMethod> metaMethods = script.getMetaClass().getMethods();
            for (MetaMethod method : metaMethods) {
                if (method instanceof CachedMethod) {
                    Method javaMethod = ((CachedMethod) method).getCachedMethod();
                    Annotation[] annotations = javaMethod.getAnnotations();
                    for (Annotation annotation : annotations) {
                        AnnotationParserFactory.parse(parsePhase, app, annotation, file, javaMethod, object);
                    }
                }
            }
        } else {
            Annotation[] annotations = object.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                AnnotationParserFactory.parse(parsePhase, app, annotation, file, object.getClass(), object);
            }
            Method[] methods = object.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Page.class)) {
                    Annotation[] ans = method.getAnnotations();
                    for (Annotation annotation : ans) {
                        AnnotationParserFactory.parse(parsePhase, app, annotation, file, method, object);
                    }
                }
            }
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                Annotation[] ans = field.getAnnotations();
                for (Annotation annotation : ans) {
                    AnnotationParserFactory.parse(parsePhase, app, annotation, file, field, object);
                }
            }
        }
    }

}
