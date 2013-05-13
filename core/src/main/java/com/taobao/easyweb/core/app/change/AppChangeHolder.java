package com.taobao.easyweb.core.app.change;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 ÏÂÎç9:35
 */
public class AppChangeHolder {

    private static Map<Priority, List<AppChangeListener>> listeners;

    static {
        listeners = new HashMap<Priority, List<AppChangeListener>>();
        String file = "META-INF/services/com.taobao.easyweb.core.app.AppChangeListener";
        try {
            Enumeration<URL> list = AppChangeHolder.class.getClassLoader().getResources(file);
            if (list != null) {
                while (list.hasMoreElements()) {
                    URL conf = list.nextElement();
                    String content = IOUtils.toString(conf.openStream());
                    if (StringUtils.isBlank(content)) {
                        continue;
                    }
                    String[] classes = content.split("\r?\n");
                    for (String className : classes) {
                        try {
                            Class<?> clazz = AppChangeHolder.class.getClassLoader().loadClass(className);
                            Object obj = clazz.newInstance();
                            if (obj instanceof AppChangeListener) {
                                AppChangeListener listener = (AppChangeListener) obj;
                                listener.init();
                                Priority priority = Priority.DEFAULT;
                                if (clazz.isAnnotationPresent(ListenerPriority.class)) {
                                    priority = clazz.getAnnotation(ListenerPriority.class).value();
                                }
                                List<AppChangeListener> l = listeners.get(priority);
                                if (l == null) {
                                    l = new ArrayList<AppChangeListener>();
                                    listeners.put(priority, l);
                                }
                                l.add(listener);
                            }
                        } catch (Exception e) {
                            AppLogger.getLogger().error("init AppChangeListener Error, Class " + className, e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.getLogger().error("",e);
        }
    }

    public static void stop(App app) {
        for (Priority priority : Priority.getAll()) {
            List<AppChangeListener> list = listeners.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (AppChangeListener listener : list) {
                listener.stop(app);
            }
        }
    }

    public static void remove(App app) {
        for (Priority priority : Priority.getAll()) {
            List<AppChangeListener> list = listeners.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (AppChangeListener listener : list) {
                listener.remove(app);
            }
        }
    }

    public static void start(App app) {
        for (Priority priority : Priority.getAll()) {
            List<AppChangeListener> list = listeners.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (AppChangeListener listener : list) {
                listener.start(app);
            }
        }
    }

    public static void deployError(App app) {
        for (Priority priority : Priority.getAll()) {
            List<AppChangeListener> list = listeners.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (AppChangeListener listener : list) {
                listener.deployError(app);
            }
        }
    }

}
