package com.taobao.easyweb.core.groovy.groovyobject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 ����10:23
 * <p/>
 * ��groovy�ļ������ʱ���¼�ļ���classloader�е�����
 */
public class FileMainClass {
    private static Map<String, String> fileMainClass = new ConcurrentHashMap<String, String>();

    public static void set(String file, String mainClass) {
        fileMainClass.put(file, mainClass);
    }

    public static String get(String file) {
        return fileMainClass.get(file);
    }

}
