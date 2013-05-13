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

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ClassUtils;

import com.taobao.easyweb.core.Configuration;

public class CharsetFilter implements Filter {

    private final static boolean responseSetCharacterEncodingAvailable = ClassUtils.hasMethod(HttpServletResponse.class, "setCharacterEncoding", new Class[]{String.class});

    private String encoding = Configuration.getHttpCharset();

    private boolean forceEncoding = false;

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setForceEncoding(boolean forceEncoding) {
        this.forceEncoding = forceEncoding;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        //jquery ajax«Î«Û
        String xrw = request.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(xrw) && xrw.equals("XMLHttpRequest")) {
            request.setCharacterEncoding("utf-8");
        } else if (this.encoding != null && (this.forceEncoding || request.getCharacterEncoding() == null)) {
            request.setCharacterEncoding(this.encoding);
            if (this.forceEncoding && responseSetCharacterEncodingAvailable) {
                response.setCharacterEncoding(this.encoding);
            }
        }
        arg2.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
}
