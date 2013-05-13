package com.taobao.easyweb.web.filter;

import org.mortbay.jetty.handler.AbstractHandler;

import com.taobao.easyweb.core.agent.AgentFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EasywebHandler extends AbstractHandler {

	/**
	 * Handle a request.
	 * 
	 * @param target
	 *            The target of the request - either a URI or a name.
	 * @param request
	 *            The request either as the {@link org.mortbay.jetty.Request}
	 *            object or a wrapper of that request. The
	 *            {@link org.mortbay.jetty.HttpConnection#getCurrentConnection()}
	 *            method can be used access the Request object if required.
	 * @param response
	 *            The response as the {@link org.mortbay.jetty.Response} object
	 *            or a wrapper of that request. The
	 *            {@link org.mortbay.jetty.HttpConnection#getCurrentConnection()}
	 *            method can be used access the Response object if required.
	 * @param dispatch
	 *            The dispatch mode: {@link #REQUEST}, {@link #FORWARD},
	 *            {@link #INCLUDE}, {@link #ERROR}
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */
	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
		Filter filter;
		if (target.startsWith("/ewagent/")) {
			filter = new AgentFilter();
		} else {
			filter = new EasywebFilter();
		}
		filter.init(null);
		filter.doFilter((ServletRequest) request, (ServletResponse) response, new FilterChain() {
			@Override
			public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
			}
		});

	}
}
