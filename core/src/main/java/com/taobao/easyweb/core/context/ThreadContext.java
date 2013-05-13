package com.taobao.easyweb.core.context;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThreadContext {

    private static ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();

    public static void init(HttpServletRequest request, HttpServletResponse response) {
        setRequest(request);
        //放置response，可以在groovy中设置头部信息
        setResponse(response);
        App app = AppContainer.getAppInfo(request.getServerName(), request.getRequestURI());
        setApp(app);
    }

    public static void destroy() {
        clean();
    }

    public static Context getContext() {
        if (threadLocal.get() == null) {
            threadLocal.set(new Context());
        }
        return threadLocal.get();
    }

    public static void setContext(Context context){
        threadLocal.set(context);
    }

    public static void setRequest(HttpServletRequest request) {
        getContext().setRequest(request);
    }

    public static void setApp(App app) {
        getContext().setApp(app);
    }

    /**
     * 给外面put上下文
     *
     * @param key
     * @param value
     */
    public static void putContext(String key, Object value) {
        getContext().putContext(key, value);
    }

    public static void clean() {
        if (threadLocal != null) {
            threadLocal.remove();
        }
    }

    public static void setResponse(HttpServletResponse response) {
        getContext().setResponse(response);
    }

}
