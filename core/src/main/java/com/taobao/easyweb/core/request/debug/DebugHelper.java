package com.taobao.easyweb.core.request.debug;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppContainer;
import com.taobao.easyweb.core.bean.BeanFactory;
import com.taobao.easyweb.core.request.PageMethod;
import com.taobao.easyweb.core.request.RequestMapping;
import com.taobao.easyweb.core.velocity.VelocityEngine;
import com.taobao.easyweb.core.velocity.VmToolFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 ����11:21
 */
@Component
public class DebugHelper {

    private static String code;
    private static List<String> messages = new LinkedList<String>();

    static {
        try {
            InputStream in = DebugHelper.class.getClassLoader().getResourceAsStream("com/taobao/easyweb/core/request/debug/debug.vm");
            code = IOUtils.toString(in);
        } catch (Exception e) {
            code = "code not found, error message is " + e.getMessage();
        }
        messages.add("������ϵ��<a target=\"_blank\" href=\"http://www.taobao.com/webww/ww.php?ver=3&touid=%E5%8D%95%E9%80%9A&siteid=cntaobao&status=1&charset=utf-8\"><img border=\"0\" src=\"http://amos.alicdn.com/realonline.aw?v=2&uid=%E5%8D%95%E9%80%9A&site=cntaobao&s=1&charset=utf-8\" alt=\"���������ҷ���Ϣ\" /></a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/issues/new\">��������</a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/issues/new\">�鿴��������</a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/wiki/%E9%97%AE%E9%A2%98%E6%8E%92%E6%9F%A5\">�鿴Easyweb�ĵ�</a>");
        messages.add("��лʹ��Easyweb����ӭ�����������ͽ��飡��");
    }

    @Resource
    private VelocityEngine velocityEngine;

    public String pageNotFount(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        Map<String, PageMethod> pages = RequestMapping.getAppPages();
        String msg = "ҳ�� ��" + request.getRequestURI() + "�� û������";
        if (pages != null) {
            for (String url : pages.keySet()) {
                if (url.endsWith(request.getRequestURI())) {
                    PageMethod pageMethod = pages.get(url);
                    StringBuilder sb = new StringBuilder();
                    sb.append("����ҳ�桾" + url + "��<br>");
                    sb.append("��ȷ���ڡ�").append(pageMethod.getFile().getAbsolutePath());
                    sb.append("���ļ��еġ�").append(pageMethod.getMethod()).append("��������@Pageע��������ȷ<br>");
                    sb.append("<b style='color:red'>�Ƿ�method���ò���ȷ</b><br>");
                    sb.append("�ڴ��ĵ�ַΪ��").append(url).append("����ʵ��Ϊ��").append(RequestMapping.getUrl(request)).append("��");
                    msg = sb.toString();
                }
            }
        }
        debugInfo.addErrorInfo(new Info("��������", "ҳ�治����"));
        debugInfo.addErrorInfo(new Info("ԭ��˵��", msg));
        return render(debugInfo);
    }

    public String pipelineException(HttpServletRequest request, Throwable e) {
        DebugInfo debugInfo = getCommonInfo(request);
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        debugInfo.addErrorInfo(new Info("��������", "pipelineִ��һ��"));
        debugInfo.addErrorInfo(new Info("������Ϣ", "���� " + request.getRequestURL() + " �����쳣"));
        debugInfo.addErrorInfo(new Info("��ϸ��ջ", writer.toString(), 1));
        return render(debugInfo);
    }

    public String serverError(HttpServletRequest request, Throwable e) {
        DebugInfo debugInfo = getCommonInfo(request);
        StringWriter writer = new StringWriter();
        PageMethod method = RequestMapping.getPageMthod(request);
        e.printStackTrace(new PrintWriter(writer));
        debugInfo.addErrorInfo(new Info("��������", "����˴���"));
        debugInfo.addErrorInfo(new Info("������Ϣ", "���� " + request.getRequestURL() + " �����쳣"));
        debugInfo.addErrorInfo(new Info("groovy�ļ�", method.getFile().getAbsolutePath()));
        debugInfo.addErrorInfo(new Info("ִ�еķ���", method.getMethod()));
        debugInfo.addErrorInfo(new Info("��ϸ��ջ", writer.toString(), 1));
        return render(debugInfo);
    }

    public String appStarting(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        debugInfo.addErrorInfo(new Info("��������", "Ӧ������������"));
        return render(debugInfo);
    }

    public String deployError(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        debugInfo.addErrorInfo(new Info("��������", "Ӧ������ʧ��"));
        return render(debugInfo);
    }

    public String appNotExist(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        debugInfo.addErrorInfo(new Info("��������", "Ӧ�ò�����"));
        return render(debugInfo);
    }

    public String appRemove(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        debugInfo.addErrorInfo(new Info("��������", "Ӧ�ñ����ߣ�ɾ����"));
        return render(debugInfo);
    }

    private DebugInfo getCommonInfo(HttpServletRequest request) {
        DebugInfo debugInfo = new DebugInfo(request);
        setAppLog(debugInfo);
        debugInfo.setAppInfos(deployInfo());
        debugInfo.addCommonInfo(new Info("������Ϣ", Configuration.print(), 2, 200));
        return debugInfo;
    }

    private List<AppInfo> deployInfo() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        Collection<App> apps = AppContainer.getApps();
        for (App app : apps) {
            AppInfo info = new AppInfo(app);
            info.setBeans(BeanFactory.getBeans(app));
            info.setVmTools(VmToolFactory.getAppTools(app.getAppKey()));
            info.setPages(RequestMapping.getAppPages(app.getAppKey()));
            list.add(info);
        }
        return list;
    }

    private void setAppLog(DebugInfo info) {
        if (info.getApp() == null) {
            return;
        }
        File log = new File(Configuration.getAppLog(info.getApp().getAppKey()));
        if (!log.exists()) {
            return;
        }
        info.addCommonInfo(new Info("��־Ŀ¼", log.getAbsolutePath()));
        try {
            String content = IOUtils.toString(new FileInputStream(log));
            int length = content.length();
            if (length > 10000) {
                content = "...\n" + content.substring(length - 10000, length);
            }
            info.addCommonInfo(new Info("��־����", content, 2, 200));
        } catch (IOException e) {

        }

    }

    private String render(DebugInfo info) {
        if (Configuration.isOnline()) {
            //log
            return "";
        }
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("debugInfo", info);
        context.put("messages", messages);
        return velocityEngine.renderTemplate("com/taobao/easyweb/core/request/debug/debug.vm", code, context);
    }

}
