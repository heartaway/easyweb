package com.taobao.easyweb.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.bean.BeanFactory;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.core.request.RequestProcessor;

/**
 * Created with IntelliJ IDEA. User: jimmey Date: 12-11-24 Time: ÏÂÎç2:22 To
 * change this template use File | Settings | File Templates.
 */
public class EasywebFilter implements Filter {

    // private Logger logger = LoggerUtil.getLogger(EasywebFilter.class);

    private RequestProcessor processor;

    @Override
    public void init(FilterConfig config) throws ServletException {
        processor = (RequestProcessor) BeanFactory.getSpringBean("ewRequestProcessor");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            processor.process(request, response);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            ThreadContext.destroy();
        }
    }

    @Override
    public void destroy() {
    }

}
