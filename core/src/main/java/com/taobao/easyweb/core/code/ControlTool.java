package com.taobao.easyweb.core.code;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.context.Context;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.core.velocity.VelocityEngine;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component("ewControlTool")
public class ControlTool {

    @Resource(name = "ewVelocityEngine")
    private VelocityEngine velocityEngine;
    @Resource(name = "ewCodeRender")
    private CodeRender codeRender;

    public String setTemplate(String vmFile) {
        Context context = ThreadContext.getContext();
        String name = "";
        String oldCurrentPath = context.getCurrentPath();
//        String layout = context.getLayout();
        String filePath = DirectoryUtil.getDirectory(context.getCurrentPath(), vmFile);
        int i = filePath.indexOf(Configuration.getDeployPath());
        if (i >= 0) {
            name = filePath.substring(i + Configuration.getDeployPath().length());
        } else {
            name = filePath;
        }
        String groovyFile = (Configuration.getDeployPath() + name).replace(".vm", ".groovy");
        File file = new File(groovyFile);
        if (file.exists()) {
            try {
//                context.setLayout(null);
                String content = codeRender.render(file, "execute");
                return content;
            } catch (Exception e) {
                return "<!-- control error " + e.getMessage() + "-->";
            } finally {
                context.setCurrentPath(oldCurrentPath);//把当前目录会写回去
//                context.setLayout(layout);
            }
        }
        if (StringUtils.isBlank(name)) {
            return "<!-- control error " + vmFile + "-->";
        }
        return velocityEngine.renderTemplate(name, context.getContextMap());
    }

    public ControlTool addParameter(String key, Object value) {
        Context context = ThreadContext.getContext();
        context.putContext(key, value);
        return this;
    }

}
