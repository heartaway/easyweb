package com.taobao.easyweb.core.request;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.code.CodeRender;
import com.taobao.easyweb.core.code.LayoutRender;
import com.taobao.easyweb.core.context.Context;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.core.request.debug.DebugHelper;
import com.taobao.easyweb.core.request.pipeline.Pipeline;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

@Component("ewRequestProcessor")
public class RequestProcessor {

    static Map<String, String> suffixType = new HashMap<String, String>();

    static {
        suffixType.put("js", "application/x-javascript;charset=gbk");
        suffixType.put("css", "text/css;charset=gbk");
        suffixType.put("jpg", "image/jpeg");
        suffixType.put("jpeg", "image/jpeg");
        suffixType.put("ico", "image/ico");
        suffixType.put("png", "image/png");
        suffixType.put("gif", "image/gif");
    }

    private static String defaultCharset = Configuration.getHttpCharset();
    @Resource(name = "ewCodeRender")
    private CodeRender codeRender;
    @Resource
    private DebugHelper debugHelper;
    @Resource
    private LayoutRender layoutRender;

    /**
     * 对一个请求做处理
     *
     * @param request
     * @return
     * @throws IOException
     */
    public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        putMDC(request);
        ThreadContext.init(request, response);
        Context context = ThreadContext.getContext();
        App app = context.getApp();
        Logger logger = AppLogger.getLogger();
        String content = "";
        if (app == null) {
            content = debugHelper.appNotExist(request);
            logger.error("应用不存在");
        } else if (app.getStatus() == 0) {
            content = debugHelper.appStarting(request);
            logger.warn("应用正在部署中");
        } else if (app.getStatus() == -1) {
            content = debugHelper.appRemove(request);
            logger.error("应用被删除了");
        } else if (app.getStatus() == 2) {
            content = debugHelper.deployError(request);
            logger.error("应用部署失败");
        }

        if (StringUtils.isNotBlank(content)) {
            content = layoutRender.render(content);
            response(response, content);
            return;
        }

        String suffix = getSuffix(request);
        if (suffixType.containsKey(suffix)) {
            String key = request.getRequestURI();// 这个key包含了appName信息在里面的
            if (key.startsWith("/ewassets/")) {
                StringBuilder sb = new StringBuilder();
                sb.append("file:").append(app.getRootPath()).append(key.replace("/ewassets/", "/assets/"));
                key = sb.toString();
            } else {// assets路径为 /ide/assets/a.js
                // /ide/assets/
                StringBuilder sb = new StringBuilder();
                int i = key.indexOf(app.getName());
                sb.append("file:").append(app.getRootPath()).append(key.substring(i + app.getName().length(), key.length()));
                key = sb.toString();
            }
            byte[] bytes = StaticCache.get(key);

            if (bytes.length == 0) {
                response.setHeader("Content-Length", bytes.length + "");
                setStatusCode(response, HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setHeader("Content-Length", bytes.length + "");
            response.setHeader("Content-Type", suffixType.get(getSuffix(request)));
            setStatusCode(response, HttpServletResponse.SC_OK);
            response.getOutputStream().write(bytes);
        } else {
            try {
                Pipeline.invoke(context);
                PageMethod pageMethod = RequestMapping.getPageMthod(request);
                if (pageMethod == null) {
                    content = debugHelper.pageNotFount(request);
                    logger.error("请求的页面不存在");
                } else {
                    if (StringUtils.isNotBlank(pageMethod.getLayout())) {
                        context.setLayout(pageMethod.getLayout());
                    }
                    try {
                        content = codeRender.render(pageMethod.getFile(), pageMethod.getMethod());
                        content = layoutRender.render(content);
                    } catch (Throwable e) {
                        content = debugHelper.serverError(request, e);
                        AppLogger.getLogger(RequestProcessor.class).error("处理请求出错", e);
                    }
                }
            } catch (Exception e) {
                content = debugHelper.pipelineException(request, e);
                logger.error("pipeline error", e);
            }
            response(response, content);
        }
    }

    private void response(HttpServletResponse response, String content) throws Exception {
        Context context = ThreadContext.getContext();
        String redirect = context.getRedirectTo();
        if (StringUtils.isNotBlank(redirect)) {
            response.sendRedirect(redirect);
        } else if (!context.isDownload()) {
            response.setHeader("Content-Length", content.getBytes(defaultCharset).length + "");
            if (response.getContentType() == null) {
                response.setContentType("text/html;charset=" + defaultCharset);
            }
            response.setHeader("Content-Language", "zh-CN");
            write(content, response);
        }
    }

    protected void write(String content, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        IOUtils.copy(new StringReader(content), response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    private void setStatusCode(HttpServletResponse response, int code) {
        response.setStatus(code);
    }

    private String getSuffix(HttpServletRequest request) {
        String suffix = request.getRequestURI().substring(request.getRequestURI().lastIndexOf(".") + 1);
        return suffix;
    }

    private void putMDC(HttpServletRequest request) {
        MDC.put("requestURI", request.getRequestURL());
    }

}
