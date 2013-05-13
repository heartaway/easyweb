package com.taobao.easyweb.hsf;

import com.taobao.sellerservice.core.client.OnlineSellerReadService;
import com.taobao.sellerservice.core.client.SellerReadService;
import com.taobao.tae.SiteCategoryService;
import com.taobao.tae.SiteService;

import java.io.File;
import java.lang.reflect.Method;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-2 ÏÂÎç2:31
 */
public class HsfGenerate {


    private static String typeName(Class<?> type) {
        String name = type.getName();
        if (name.startsWith("java.lang.")) {
            return name.substring("java.lang.".length());
        }
        return name;
    }

    public static String gen(Class<?> interf) {
        if (!interf.isInterface()) {
            throw new IllegalArgumentException("Not a interface:" + interf);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("import com.taobao.easyweb.hsf.Hsf;\n");
        sb.append("import com.taobao.easyweb.hsf.HSFInvokeHelper;\n");
        /*sb.append("import com.taobao.hsf.ndi.model.ObjectDataWrapper;\n");
        sb.append("import com.taobao.tae.common.engine.ObjectDataWrapperHelper;\n");*/
        sb.append("\n@Hsf(name=\"\",serviceName=\"\",dailyVersion=\"\",onlineVersion=\"\")");
        sb.append("\nclass ").append(interf.getSimpleName()).append(" {\n");
        sb.append("    def hsfServiceName\n");
        sb.append("    def hsfServiceVersion\n");

        for (Method m : interf.getDeclaredMethods()) {
            //sb.append("\n    ").append(typeName(m.getReturnType())); //void foo();
            sb.append("\n    def ").append(m.getName()).append("(");
            Class<?>[] paramTypes = m.getParameterTypes();
            StringBuilder typeValue = new StringBuilder();
            StringBuilder argsValue = new StringBuilder();
            if (paramTypes.length > 0) {
                for (int i = 0; i < paramTypes.length; i++) {
                    Class<?> type = paramTypes[i];
                    sb.append("def ").append("a").append(i).append(", ");
                    typeValue.append("\"").append(type.getName()).append("\"").append(", ");
                    argsValue.append("a").append(i).append(", ");
                }
                sb.delete(sb.length() - 2, sb.length());
                typeValue.delete(typeValue.length() - 2, typeValue.length());
                argsValue.delete(argsValue.length() - 2, argsValue.length());
            }
            sb.append(")").append("{\n");
            sb.append("        def parameterTypes = [").append(typeValue).append("] as String[];\n");
            sb.append("        def args = [").append(argsValue).append("] as Object[];\n");
            sb.append("        Object res = HSFInvokeHelper.invokeHsfService(hsfServiceName, hsfServiceVersion, \"")
                    .append(m.getName()).append("\", parameterTypes, args);\n");
            sb.append("        return res;\n");
            sb.append("    }\n");
        }

        sb.append("}");
        return sb.toString();
    }

    private static void writeToLocalFile(String localGroovyServicePath, String serviceName, String content) throws Exception {
        File dir = new File(localGroovyServicePath, serviceName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, serviceName + ".groovy");
        if (!file.exists()) {
            file.createNewFile();
        }
        System.out.println(content);
//        FileUtils.saveFile(file.getAbsolutePath(), content);
    }

    private static String lowerFirstLetter(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static void main(String[] args) throws Exception {
        String localGroovyServicePath = "D:\\DevelopPackage\\sitemodules\\hsf\\groovy";

        Class<?> interf = SellerReadService.class;

//        interf = VoteService.class;


//        interf = C2BThemeActivityService.class;


        String sourceCode = gen(interf);
//        String serviceName = lowerFirstLetter(interf.getSimpleName());
//        writeToLocalFile(localGroovyServicePath, serviceName, sourceCode);
        System.out.println(sourceCode);
    }

}
