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
     * 将字符串解析为ObjectDataWrapper对象，用来作为className对象的包装，以使其能够通过hession序列化
     * @param jsonStr
     * @return
     */
    ObjectDataWrapper parse(String preDefClassType, String jsonStr);


    /**
     * @param preDefClassType 实际参数数组元素的类型
     * @param jsonStr
     * @return
     */
    ObjectDataWrapper[] parse(String preDefElementType, String[] jsonStr);


    /**
     * @param preDefClassType 实际参数List元素的类型
     * @param jsonStr
     * @return
     */
    List<ObjectDataWrapper> parse(String preDefElementType, List<String> jsonStr);


    /**
     * @param preDefClassType 实际参数Set元素的类型
     * @param jsonStr
     * @return
     */
    Set<ObjectDataWrapper> parse(String preDefElementType, Set<String> jsonStr);


    /**
     * @param preDefClassType 实际参数Map的value的类型
     * @param jsonStr
     * @return
     */
    Map<Object, ObjectDataWrapper> parse(String preDefValueType, Map<Object, String> jsonStr);
}
