package com.taobao.easyweb.security;

import com.taobao.easyweb.core.app.App;
import com.taobao.easyweb.core.groovy.GroovyEngine;
import com.taobao.easyweb.core.groovy.transformation.MethodTransformation;
import com.taobao.easyweb.security.annotation.OnPerm;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityTransformation extends MethodTransformation {

    @OnPerm
    @Override
    public void transformat(SourceUnit sourceUnit, MethodNode methodNode, App app) {
        List<MethodNode> methods = sourceUnit.getAST().getMethods();
        for (MethodNode method : methods) {
            List<AnnotationNode> annotations = method.getAnnotations();
            for (AnnotationNode annotationNode : annotations) {
                String className = annotationNode.getClassNode().getName();
                if ("".equals(className)) {

                }
            }
        }
    }

}
