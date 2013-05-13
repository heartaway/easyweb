package com.taobao.easyweb.core.groovy.transformation;

import com.taobao.easyweb.core.app.App;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

public abstract class MethodTransformation {

	public MethodTransformation() {
		MethodTransformationInvoker.add(this);
	}

	public abstract void transformat(SourceUnit sourceUnit, MethodNode methodNode,App app);

}
