package com.taobao.easyweb.core.groovy;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.core.groovy.groovyobject.GroovyObjectLoader;
import com.taobao.easyweb.core.request.Param;
import com.taobao.easyweb.core.request.RequestBean;
import groovy.lang.GroovyObject;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.reflection.CachedMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("ewGroovyEngine")
public class GroovyEngine {

    /**
     * 每个方法对应的参数数
     */
    private static Map<String, Map<String, Integer>> scriptMethods = new ConcurrentHashMap<String, Map<String, Integer>>();
    @Resource
    private GroovyObjectLoader groovyObjectLoader;

    public static void putScriptMethod(String file, String method, Integer size) {
        Map<String, Integer> methods = scriptMethods.get(file);
        if (methods == null) {
            methods = new HashMap<String, Integer>();
            scriptMethods.put(file, methods);
        }
        scriptMethods.get(file).put(method, size);
    }

    public Object execute(File file, String method) throws Exception {
        App app = ThreadContext.getContext().getApp();
        GroovyObject groovyObject = groovyObjectLoader.getObject(true, app, file);
        return groovyObject.invokeMethod(method, setParameters(groovyObject, method, file));
    }

    private Object[] setParameters(GroovyObject groovyObject, String method, File file) throws Exception {
        Map<String, Integer> methods = scriptMethods.get(file.getAbsolutePath());
        if (methods == null) {//对于Groovy class在compile的时候获取不对方法信息，这里暂时只支持无参方法调用
            return new Object[0];
        }
        int size = methods.get(method) == null ? 0 : methods.get(method);
        if (size == 0) {
            return new Object[0];
        }
        groovy.lang.MetaMethod metaMethod = groovyObject.getMetaClass().getMetaMethod(method, new Object[size]);
        Method javaMethod = ((CachedMethod) metaMethod).getCachedMethod();

        // Page page = javaMethod.getAnnotation(Page.class);
        // if (page == null) {
        // return new Object[size];
        // }

        Type[] paramTypes = javaMethod.getGenericParameterTypes();
        Annotation[][] paramAnnotations = javaMethod.getParameterAnnotations();
        Object[] params = new Object[paramTypes.length];
        if (paramTypes.length != paramAnnotations.length) {
            throw new Exception("参数格式错误");
        }
        if (paramTypes.length == 0) {
            return params;
        }
        for (int i = 0; i < paramTypes.length; i++) {
            Annotation annotation = paramAnnotations[i][0];
            if (annotation instanceof Param) {
                params[i] = typeCovert((Param) annotation, paramTypes[i]);
            } else if (annotation instanceof RequestBean) {
                params[i] = beanConvert(paramTypes[i]);
            }
        }
        return params;
    }

    private Object typeCovert(Param param, Type type) {
        String name = param.name();
        if (StringUtils.isBlank(name)) {
            name = param.value();
        }
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                Class<?> componentClazz = clazz.getComponentType();
                String[] values = ThreadContext.getContext().getRequest().getParameterValues(name);
                if (values != null) {
                    Object array = Array.newInstance(componentClazz, values.length);
                    for (int i = 0; i < values.length; i++) {
                        Array.set(array, i, convert(param, values[i], componentClazz));
                    }
                    return array;
                }
                return null;
            } else {
                String value = ThreadContext.getContext().getRequest().getParameter(name);
                return convert(param, value, clazz);
            }
        }
        return null;
    }

    private Object convert(Param param, String value, Class<?> clazz) {
        if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
            try {
                return Integer.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(param.defaultValue())) {
                    return Integer.valueOf(param.defaultValue());
                }
            }
        } else if (clazz.equals(Long.class) || clazz.equals(Long.TYPE)) {
            try {
                return Long.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(param.defaultValue())) {
                    return Long.valueOf(param.defaultValue());
                }
            }
        } else if (clazz.equals(Double.class) || clazz.equals(Double.TYPE)) {
            try {
                return Double.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(param.defaultValue())) {
                    return Double.valueOf(param.defaultValue());
                }
            }
        } else if (clazz.equals(Float.class) || clazz.equals(Float.TYPE)) {
            try {
                return Float.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(param.defaultValue())) {
                    return Float.valueOf(param.defaultValue());
                }
            }
        } else if (clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE)) {
            try {
                return Boolean.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(param.defaultValue())) {
                    return Boolean.valueOf(param.defaultValue());
                }
            }
        } else {
            if (value != null) {
                return value;
            } else if (StringUtils.isNotBlank(param.defaultValue())) {
                return param.defaultValue();
            }
        }
        return null;
    }

    private Object beanConvert(Type beanType) {
        if (beanType instanceof Class<?>) {
            Class<?> clazz = (Class<?>) beanType;
            try {
                Object bean = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field it : fields) {
                    it.setAccessible(true);
                    String name = it.getName();
                    Class<?> type = it.getType();
                    String requestValue = ThreadContext.getContext().getRequest().getParameter(name);
                    if (requestValue != null) {
                        Object propertyValue = null;
                        if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
                            try {
                                propertyValue = Integer.valueOf(requestValue);
                            } catch (Exception e) {
                                // 不合法参数不做封装
                            }
                        } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
                            try {
                                propertyValue = Long.valueOf(requestValue);
                            } catch (Exception e) {
                                // 不合法参数不做封装
                            }
                        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
                            try {
                                propertyValue = Double.valueOf(requestValue);
                            } catch (Exception e) {
                                // 不合法参数不做封装
                            }
                        } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
                            try {
                                propertyValue = Float.valueOf(requestValue);
                            } catch (Exception e) {
                                // 不合法参数不做封装
                            }
                        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
                            try {
                                propertyValue = Boolean.valueOf(requestValue);
                            } catch (Exception e) {
                                // 不合法参数不做封装
                            }
                        } else {
                            propertyValue = requestValue;
                        }
                        if (propertyValue != null) {
                            it.set(bean, propertyValue);
                        }
                    }
                }
                return bean;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

}
