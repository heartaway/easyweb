package com.taobao.easyweb.hsf.transfomer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.hsf.ndi.model.ObjectDataWrapper;


/**
 * 
 * @author linxuan
 *
 */
public interface ObjectDataWrapperParser {
    /**
     * ���ַ�������ΪObjectDataWrapper����������ΪclassName����İ�װ����ʹ���ܹ�ͨ��hession���л�
     * @param jsonStr
     * @return
     */
    ObjectDataWrapper parse(String preDefClassType, String jsonStr);


    /**
     * @param preDefClassType ʵ�ʲ�������Ԫ�ص�����
     * @param jsonStr
     * @return
     */
    ObjectDataWrapper[] parse(String preDefElementType, String[] jsonStr);


    /**
     * @param preDefClassType ʵ�ʲ���ListԪ�ص�����
     * @param jsonStr
     * @return
     */
    List<ObjectDataWrapper> parse(String preDefElementType, List<String> jsonStr);


    /**
     * @param preDefClassType ʵ�ʲ���SetԪ�ص�����
     * @param jsonStr
     * @return
     */
    Set<ObjectDataWrapper> parse(String preDefElementType, Set<String> jsonStr);


    /**
     * @param preDefClassType ʵ�ʲ���Map��value������
     * @param jsonStr
     * @return
     */
    Map<Object, ObjectDataWrapper> parse(String preDefValueType, Map<Object, String> jsonStr);
}
