package com.taobao.easyweb.core.bean;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.change.AppChangeAdapter;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.core.groovy.groovyobject.EasywebClassLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("ewBeanFactory")
public class BeanFactory extends AppChangeAdapter implements ApplicationContextAware, InitializingBean {


    private static ApplicationContext applicationContext;
    private static Map<String, Map<String, Object>> beans = new HashMap<String, Map<String, Object>>();
    @Autowired(required = false)
    private LocalBean localBean;

    public static Object getSpringBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static Object getSpringBean(String beanName, Class<?> beanType) {
        Object obj = null;
        try {
            obj = getSpringBean(beanName);
        } catch (Exception e) {
        }
        if (obj != null) {
            return obj;
        }
        Map map = applicationContext.getBeansOfType(beanType);
        if (map.containsKey(beanName)) {
            return map.get(beanName);
        }
        if (!map.isEmpty()) {
            return map.values().iterator().next();
        }
        return null;
    }

    public static void regist(App app, String name, Object bean) {
        /**
         * 不是easyweb classloader加载的，不加载进bean中来
         */
        ClassLoader classLoader = bean.getClass().getClassLoader();
        if (!(classLoader instanceof EasywebClassLoader)) {
            return;
        }

        EasywebClassLoader easywebClassLoader = (EasywebClassLoader) classLoader;
        if (easywebClassLoader.isWeb()) {//是web的classloader，也不注册bean
            return;
        }

        Map<String, Object> appBeans = getAppBeans(app.getAppKey());
        appBeans.put(name, bean);
    }

    public static Object getBean(String name) {
//        Object obj = getAppBeans(ThreadContext.getContext().getApp().getAppKey()).get(name);
//        if (obj.getClass().isAnnotationPresent(Bean.class)) {
//            Bean bean = obj.getClass().getAnnotation(Bean.class);
//            if (!bean.singleton()) {
//
//            }
//        }
        return getAppBeans(ThreadContext.getContext().getApp().getAppKey()).get(name);
    }

    public static Object getAppBean(String appKey, String beanName) {
        Map<String, Object> appBeans = getAppBeans(appKey);
        return appBeans.get(beanName);
    }

    private static Map<String, Object> getAppBeans(String appKey) {
        Map<String, Object> appBeans = beans.get(appKey);
        if (appBeans == null) {
            appBeans = new HashMap<String, Object>();

            beans.put(appKey, appBeans);
        }
        if (!"local".equals(appKey)) {
            appBeans.putAll(getAppBeans("local"));
        }

        return appBeans;
    }

    public static Map<String, Object> getBeans(App app) {
        String key = app == null ? "local" : app.getAppKey();
        return getAppBeans(key);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanFactory.applicationContext = applicationContext;
    }

    @Override
    public void remove(App app) {
        beans.remove(app.getAppKey());
    }

    @Override
    public void stop(App app) {
        beans.remove(app.getAppKey());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (localBean == null) {
            return;
        }
        if (localBean.getAllLocalBeans()) {
            for (String bean : applicationContext.getBeanDefinitionNames()) {
                try {
                    Object obj = applicationContext.getBean(bean);
                    getAppBeans("local").put(bean, obj);
                } catch (Throwable e) {
                }
            }
        } else if (localBean.getLocalBeans() != null && !localBean.getLocalBeans().isEmpty()) {
            for (Map.Entry<String, String> entry : localBean.getLocalBeans().entrySet()) {
                try {
                    Object obj = applicationContext.getBean(entry.getValue());
                    if (obj != null) {
                        getAppBeans("local").put(entry.getKey(), obj);
                    }
                } catch (Exception e) {

                }
            }
        }
    }

}
