package com.taobao.easyweb.core.code;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.context.Context;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.core.groovy.GroovyEngine;
import com.taobao.easyweb.core.velocity.VelocityEngine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * groovy代码执行入口
 *
 * @author jimmey
 */
@Component("ewCodeRender")
public class CodeRender {

    @Resource(name = "ewGroovyEngine")
    private GroovyEngine groovyEngine;
    @Resource(name = "ewVelocityEngine")
    private VelocityEngine velocityEngine;

    /**
     * 渲染页面，如果有layout也在这里进行渲染
     *
     * @return
     * @throws IOException
     */
    public String render(File file, String method) throws Exception {
        StringWriter writer = new StringWriter();
        render(file, method, writer);
//        String content = writer.toString();
//        Context context = ThreadContext.getContext();
//
//        String layout = context.getLayout();
//        if (layout != null && layout.endsWith(".vm") && context.getCurrentPath() != null) {
//            String layoutFile = DirectoryUtil.getDirectory(context.getCurrentPath(), layout).replace(Configuration.getDeployPath(), "");
//            context.putContext("screen_placeholder", content);
//            int i = layoutFile.lastIndexOf("/");
//            context.setCurrentPath(layoutFile.substring(0, i));
//            return velocityEngine.renderTemplate(layoutFile, context.getContextMap());
//        }
        return writer.toString();
    }

//    public String renderWithLayout(File file, String method) throws Exception {
//        StringWriter writer = new StringWriter();
//        render(file, method, writer);
//        String content = writer.toString();
//        Context context = ThreadContext.getContext();
//
//        String layout = context.getLayout();
//        if (layout != null && layout.endsWith(".vm") && context.getCurrentPath() != null) {
//            String layoutFile = DirectoryUtil.getDirectory(context.getCurrentPath(), layout).replace(Configuration.getDeployPath(), "");
//            context.putContext("screen_placeholder", content);
//            int i = layoutFile.lastIndexOf("/");
//            context.setCurrentPath(layoutFile.substring(0, i));
//            return velocityEngine.renderTemplate(layoutFile, context.getContextMap());
//        }
//        return content;
//    }

    public void render(File groovyFile, String method, Writer writer) throws Exception {
        Context context = ThreadContext.getContext();
        context.setCurrentPath(DirectoryUtil.getFileParentPath(groovyFile));
        Object obj = groovyEngine.execute(groovyFile, method);
        if (obj instanceof String) {
            writer.write((String) obj);
            return;
        }
        velocityEngine.renderTemplate(getTemplateName(groovyFile), context.getContextMap(), writer);
    }

    private String getTemplateName(File groovyFile) {
        String p = DirectoryUtil.getFilePath(groovyFile);
        int i = p.indexOf(Configuration.getDeployPath());
        if (i >= 0) {
            p = p.substring(i + Configuration.getDeployPath().length());
        }
        return p.replace(".groovy", ".vm");
    }

}
