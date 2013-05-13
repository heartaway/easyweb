package com.taobao.easyweb.core.groovy.transformation;

import java.util.List;

import com.taobao.easyweb.core.app.App;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.groovy.GroovyEngine;

@Component
public class ParameterTransformation extends MethodTransformation {

    @Override
    public void transformat(SourceUnit sourceUnit, MethodNode methodNode, App app) {
        List<MethodNode> methods = sourceUnit.getAST().getMethods();
        for (MethodNode method : methods) {
            GroovyEngine.putScriptMethod(sourceUnit.getName(), method.getName(), method.getParameters().length);
        }
    }

}
