package com.taobao.easyweb.core.groovy;

import groovy.lang.Interceptor;
import groovy.lang.MetaMethod;
import groovy.lang.Script;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Map;

import org.codehaus.groovy.reflection.CachedMethod;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.groovy.annotation.MethodAnnotationInvokeFactory;

@Component("ewMethodInterceptor")
public class MethodInterceptor implements Interceptor {

    public Object beforeInvoke(Object object, String methodName, Object[] arguments) {
        if (object instanceof Script) {
            Script script = (Script) object;

            MetaMethod metaMethod = script.getMetaClass().getMetaMethod(methodName, arguments);
            if (metaMethod == null || !(metaMethod instanceof CachedMethod)) {
                return null;
            }

            Method javaMethod = ((CachedMethod) metaMethod).getCachedMethod();
            MethodAnnotationInvokeFactory.invoke(javaMethod);
        } else {
            try {
                Method method = object.getClass().getMethod(methodName, Array.class);
                MethodAnnotationInvokeFactory.invoke(method);
            } catch (Exception e) {

            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Object afterInvoke(Object object, String methodName, Object[] arguments, Object result) {
//		if (result instanceof Map<?, ?> && arguments.length == 1 && arguments[0] instanceof Map<?, ?>) {
//			Map<String, Object> context = (Map<String, Object>) arguments[0];
//			context.putAll((Map<String, Object>) result);
//		}
        return result;
    }

    public boolean doInvoke() {
        return true;
    }

}
