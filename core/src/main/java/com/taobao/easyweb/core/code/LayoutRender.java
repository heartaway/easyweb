package com.taobao.easyweb.core.code;

import com.taobao.easyweb.core.Configuration;
import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.app.AppContainer;
import com.taobao.easyweb.core.context.Context;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.core.velocity.VelocityEngine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-2 ÏÂÎç2:11
 */
@Component
public class LayoutRender {
    @Resource
    private VelocityEngine velocityEngine;

    public String render(String screenPlaceholder) {
        Context context = ThreadContext.getContext();
        String layout = context.getLayout();

        if (layout != null && layout.endsWith(".vm") && context.getCurrentPath() != null) {
            String layoutFile = "";
            int a = layout.indexOf(":");
            if (a > 0) {
                App app = AppContainer.getApp(layout.substring(0, a));
                if (app == null) {
                    throw new RuntimeException("outer layout app not exist");
                }
                layout = layout.substring(a + 1);
                layoutFile = DirectoryUtil.getDirectory(app.getRootPath(), layout).replace(Configuration.getDeployPath(), "");
            } else {
                layoutFile = DirectoryUtil.getDirectory(context.getCurrentPath(), layout).replace(Configuration.getDeployPath(), "");
            }
            context.putContext("screen_placeholder", screenPlaceholder);
            int i = layoutFile.lastIndexOf("/");
            context.setCurrentPath(layoutFile.substring(0, i));
            return velocityEngine.renderTemplate(layoutFile, context.getContextMap());
        }
        return screenPlaceholder;
    }

}
