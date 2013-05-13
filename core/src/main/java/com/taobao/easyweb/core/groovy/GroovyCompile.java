package com.taobao.easyweb.core.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.FileSystemCompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-23 下午5:40
 * <p/>
 * groovy编译测试类
 */
public class GroovyCompile {

    public static void main(String[] v) throws Exception {
        String classpath = "/Users/jimmey/easyweb/classes";
        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setTargetDirectory(classpath);
        FileSystemCompiler compiler = new FileSystemCompiler(configuration);
        List<String> list = new ArrayList<String>();
        list.add("/Users/jimmey/workspace/usp/uspapps/groovyloader/src/main/java/before/Index.groovy");
        list.add("/Users/jimmey/workspace/usp/uspapps/groovyloader/src/main/java/bean/Bean.groovy");
        list.add("/Users/jimmey/workspace/usp/uspapps/groovyloader/src/main/java/beforebean/CBean.groovy");
        list.add("/Users/jimmey/workspace/usp/uspapps/groovyloader/src/main/java/XBean.groovy");
        list.add("/Users/jimmey/workspace/usp/uspapps/groovyloader/src/main/java/Script.groovy");
        try {
            compiler.compile(list.toArray(new String[list.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        MyGroovyClassLoader groovyClassLoader = new MyGroovyClassLoader();
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        groovyClassLoader.addClasspath(classpath);
        Class<?> clazz = groovyClassLoader.loadClass("before.Index");
        System.out.println(clazz.getClassLoader());
        System.out.println(groovyClassLoader.toString());
        System.out.println(clazz);
        System.out.println(groovyClassLoader.loadClass("XBean"));
        System.out.println(groovyClassLoader.loadClass("Script"));
//        Class<?> xx = groovyClassLoader.loadClass("Script");
//        GroovyObject obj = (GroovyObject)xx.newInstance();
//        if(obj instanceof Script){
//            System.out.println( InvokerHelper.invokeMethod(obj,"exe",null));
//        }
//
        GroovyClassLoader loader = new GroovyClassLoader(groovyClassLoader);
        File file = new File("/Users/jimmey/workspace/usp/uspapps/groovyloader/src/main/java/before/Index.groovy");
        GroovyCodeSource codeSource = new GroovyCodeSource(file, "GBK");
        /**
         * 这里不用groovy.lang.MyGroovyClassLoader.parseClass(File)，
         * 因为GroovyCodeSource默认cachable=true
         */
//            groovyResourceLoader.setApp(app);

        Class<?> scriptClass = loader.parseClass(codeSource, false);
        System.out.println(scriptClass.getClassLoader());
        GroovyObject object = (GroovyObject) scriptClass.newInstance();
        System.out.println(object.invokeMethod("test", null));
    }

}
