package com.taobao.easyweb.hsf.transfomer;


import java.util.Collection;
import java.util.List;
import java.util.Map;



/**
 * 将返回值的Map转换为json串
 * 
 * @author linxuan
 *
 */
public class DefaultJsonTransformer implements HsfServiceDef.ReturnTransformer {

    private String jsonStringQuote = "\"";


    @Override
    public Object transformer(String hsfReturnType, Object hsfReturnObject) {
        if (hsfReturnObject instanceof Map) {
            StringBuilder sb = new StringBuilder();
            JsonParser.toJsonStr(hsfReturnObject, sb, " ", 0, jsonStringQuote);
            return sb.toString();
        }
        else if (hsfReturnObject instanceof List) {
            if (hasElement((List<?>) hsfReturnObject, Map.class)) {
                StringBuilder sb = new StringBuilder();
                JsonParser.toJsonStr(hsfReturnObject, sb, " ", 0, jsonStringQuote);
                return sb.toString();
            }
        }
        return hsfReturnObject;
    }


    public static boolean hasElement(Collection<?> c, Class<?> type) {
        if (c.isEmpty()) {
            return false;
        }
        for (Object obj : c) {
            if (obj != null && obj.getClass() == type) {
                return true;
            }
        }
        return false;
    }


    public void setJsonStringQuote(String jsonStringQuote) {
        this.jsonStringQuote = jsonStringQuote;
    }

}
