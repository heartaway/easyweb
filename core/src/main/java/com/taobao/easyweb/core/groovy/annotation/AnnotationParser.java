package com.taobao.easyweb.core.groovy.annotation;

import com.taobao.easyweb.core.app.App;
import groovy.lang.GroovyObject;

import java.io.File;
import java.lang.annotation.Annotation;

/**
 * Annotation����
 *
 * @author jimmey
 */
public abstract class AnnotationParser {

    private ParsePhase[] parsePhases;

    public AnnotationParser() {
        this(new ParsePhase[]{ParsePhase.Init});
    }

    public AnnotationParser(ParsePhase[] parsePhases) {
        this.parsePhases = parsePhases;
        AnnotationParserFactory.regist(this);
    }

    public ParsePhase[] getParsePhases() {
        return parsePhases;
    }

    public void setParsePhases(ParsePhase[] parsePhases) {
        this.parsePhases = parsePhases;
    }

    /**
     * �Ƿ��Ǳ�parser֧�ֵ�annotation
     *
     * @param annotation
     * @return
     */
    public abstract boolean isParse(Annotation annotation);

    /**
     * ����ǣ�������Ӧ�Ĵ���
     *
     * @param annotation
     * @param file         �ļ���ַ
     * @param target       ע���Ӧ�Ķ���Class��Method
     * @param groovyObject groovy����
     */
    public abstract void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject);

    public enum ParsePhase {
        Init,//groovyObject��ʼ���׶�
        Ioc//groovy����ע��׶�
    }

}
