package com.taobao.easyweb.core.velocity;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.app.AppLogger;
import com.taobao.easyweb.core.code.ControlTool;
import com.taobao.easyweb.core.code.common.CommonTool;
import com.taobao.easyweb.core.context.ThreadContext;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA. User: jimmey Date: 12-11-23 Time: 下午10:01 To
 * change this template use File | Settings | File Templates.
 */
@Component("ewVelocityEngine")
public class VelocityEngine implements InitializingBean {


    private org.apache.velocity.app.VelocityEngine velocityEngine;
    @Resource(name = "ewCommonTool")
    private CommonTool commonTool;
    @Resource(name = "ewControlTool")
    private ControlTool controlTool;
    @Resource
    private Map<String, Object> velocityTools;
    private PageAttributeTool page = new PageAttributeTool();

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * directive.set.null.allowed=true input.encoding=GBK
         * output.encoding=GBK resource.loader = file file.resource.loader.class
         * = org.apache.velocity.runtime.resource.loader.FileResourceLoader
         * file.resource.loader.path =/home/admin/easyweb/components
         * file.resource.loader.cache = true
         * file.resource.loader.modificationCheckInterval = 2
         */
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("directive.set.null.allowe", "true");
        velocityProperties.setProperty("input.encoding", "GBK");
        velocityProperties.setProperty("output.encoding", "GBK");
        velocityProperties.setProperty("resource.loader", "file,string");
        velocityProperties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        velocityProperties.setProperty("file.resource.loader.path", Configuration.getDeployPath());
        velocityProperties.setProperty("file.resource.loader.cache", "true");
        velocityProperties.setProperty("file.resource.loader.modificationCheckInterval", "3");

        velocityProperties.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");

        velocityEngine = new org.apache.velocity.app.VelocityEngine();
        velocityEngine.init(velocityProperties);
        if (velocityTools == null) {
            velocityTools = new HashMap<String, Object>();
        }
        velocityTools.put("commonTool", commonTool);
        velocityTools.put("controlTool", controlTool);
        velocityTools.put("assetsTool", page);
        velocityTools.put("pageTool", page);
    }

    /**
     * @param name    需要从 Configuration.getDeployPath()下开始，这样才能保持名称的唯一
     * @param context
     * @return
     */
    public String renderTemplate(String name, Map<String, Object> context) {
        StringWriter writer = new StringWriter();
        renderTemplate(name, context, writer);
        return writer.toString();
    }

    public void renderTemplate(String name, Map<String, Object> context, Writer writer) {
        Logger logger = AppLogger.getLogger("GroovyScript");
        VelocityContext velocityContext = new VelocityContext(context);
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }
        // velocityContext.put("codeInclude", codeInclude);
        if (velocityTools != null) {
            for (Map.Entry<String, Object> entry : velocityTools.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }
        }
        Map<String, Object> appTools = VmToolFactory.getAppTools(ThreadContext.getContext().getAppName());
        if (appTools != null) {
            for (Map.Entry<String, Object> entry : appTools.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }
        }

        try {
            velocityEngine.getTemplate(name).merge(velocityContext, writer);
        } catch (ResourceNotFoundException e) {
            logger.error(e);
        } catch (ParseErrorException e) {
            logger.error(e);
        } catch (MethodInvocationException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public String renderTemplate(String name, String code, Map<String, Object> context) {
        StringResourceRepository vsRepository = StringResourceLoader.getRepository();
        vsRepository.putStringResource(name, code, "GBK");
        StringWriter writer = new StringWriter();
        renderTemplate(name, context, writer);
        return writer.toString();
    }

}
