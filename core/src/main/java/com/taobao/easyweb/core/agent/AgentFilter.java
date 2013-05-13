package com.taobao.easyweb.core.agent;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 管理后台，功能包括<br>
 * 1. 监控检查<br>
 * 2. App部署<br>
 * 
 * 所有返回结果都是json
 * 
 * @author jimmey
 * 
 */
public class AgentFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {

	}

	/**
	 * curl "localhost:7001/ewagent/deploy"<br>
	 * curl "localhost:7001/ewagent/undeploy"<br>
	 */
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
//		HttpServletRequest request = (HttpServletRequest) arg0;
//		HttpServletResponse response = (HttpServletResponse) arg1;
//		String uri = request.getRequestURI();
//		if (!uri.startsWith("/ewagent/")) {
//			arg2.doFilter(arg0, arg1);
//			return;
//		}
//		String[] v = uri.split("/");
//		Result<String> result = new Result<String>(true);
//		if (v.length != 3) {
//			result.setSuccess(false);
//			result.addErrorMessage("uri error");
//		} else {
//			try {
//				if ("deploy".equals(v[2])) {// 部署app
////					String svnurl = getParameter(request, "svnurl");
////					String username = getParameter(request, "username");
////					String password = getParameter(request, "password");
//					String appName = getParameter(request, "appName");
//					result = AppDeployer.deploy(appName);
//				} else if ("undeploy".equals(v[2])) {// 卸载app
//					String appName = getParameter(request, "appName");
//					String appVersion = getParameter(request, "appVersion");
//					result = AppDeployer.undeploy(appName, appVersion);
//				} else if ("preload".equals(v[2])) {// 健康检查
//					result.setModule("preload_ok");
//				} else if ("setVersion".equals(v[2])) {// 设置app默认版本
//					String appName = getParameter(request, "appName");
//					String appVersion = getParameter(request, "appVersion");
//					AppContainer.setCurrentVersion(appName, appVersion);
//				}
//			} catch (Exception e) {
//				result.setSuccess(false);
//				result.addErrorMessage(e.getMessage());
//			}
//		}
//		String content = new JsonBuilder(result).toString();
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.setHeader("Content-Length", content.getBytes().length + "");
//		response.setHeader("Content-Type", "text/html;charset=gbk");
//		IOUtils.copy(new StringReader(content), response.getOutputStream());
//		response.getOutputStream().flush();
//		response.getOutputStream().close();
    }

	private String getParameter(HttpServletRequest request, String name) throws Exception {
		String value = request.getParameter(name);
		if (value == null) {
			throw new Exception("param " + name + " is null");
		}
		return value;
	}

}
