package com.taobao.easyweb.core.groovy.transformation;

import com.taobao.easyweb.core.groovy.groovyobject.FileMainClass;
import com.taobao.easyweb.core.groovy.GroovyEngine;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.util.ArrayList;
import java.util.List;

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
public class MethodTransformationInvoker extends CodeVisitorSupport implements ASTTransformation {

    private static List<MethodTransformation> transformations = new ArrayList<MethodTransformation>();

    public static void add(MethodTransformation transformation) {
        transformations.add(transformation);
    }

    public void visit(ASTNode[] nodes, SourceUnit source) {
        FileMainClass.set(source.getName(), source.getAST().getMainClassName());
        List<MethodNode> methods = source.getAST().getMethods();
        if (!methods.isEmpty()) {
            for (MethodNode method : methods) {
                for (MethodTransformation transformation : transformations) {
                    transformation.transformat(source, method, null);
                }
            }
        }
        List<ClassNode> classNodes = source.getAST().getClasses();
        if (!classNodes.isEmpty()) {
            for (ClassNode classNode : classNodes) {
                if (classNode instanceof InnerClassNode) {
                    continue;
                }
                for (MethodNode method : classNode.getMethods()) {
                    GroovyEngine.putScriptMethod(source.getName(), method.getName(), method.getParameters().length);
//                    for (MethodTransformation transformation : transformations) {
////                        transformation.transformat(source, method);
//                    }
                }
            }
        }

    }
}
