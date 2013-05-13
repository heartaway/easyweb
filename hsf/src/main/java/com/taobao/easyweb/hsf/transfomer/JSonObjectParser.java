package com.taobao.easyweb.hsf.transfomer;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.hsf.ndi.model.ObjectDataWrapper;


/**
 * 
 * @author linxuan
 *
 */
public class JSonObjectParser implements ObjectDataWrapperParser {
    private static final Log log = LogFactory.getLog(JSonObjectParser.class);

    @SuppressWarnings("unchecked")
    @Override
    public ObjectDataWrapper parse(String preDefClassType, String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        JsonParser parser = new JsonParser(jsonStr);
        Object structure;
        try {
            structure = parser.parse();
        }
        catch (Exception e) {
            log.error("", e);
            return null;
        }
        if (!(structure instanceof Map)) {
            throw new IllegalArgumentException(preDefClassType + "'s jsonStr is not Map:" + jsonStr);
        }
        return ObjectDataWrapperHelper.toObjectDataWrapper(preDefClassType, (Map<String, Object>) structure);
    }


    @Override
    public List<ObjectDataWrapper> parse(String preDefClassType, List<String> jsonStr) {
        List<ObjectDataWrapper> res = new LinkedList<ObjectDataWrapper>();
        for (String json : jsonStr) {
            res.add(parse(preDefClassType, json));
        }
        return res;
    }


    @Override
    public ObjectDataWrapper[] parse(String preDefClassType, String[] jsonStr) {
        ObjectDataWrapper[] res = new ObjectDataWrapper[jsonStr.length];
        for (int i = 0; i < jsonStr.length; i++) {
            res[i] = parse(preDefClassType, jsonStr[i]);
        }
        return res;
    }


    @Override
    public Set<ObjectDataWrapper> parse(String preDefClassType, Set<String> jsonStr) {
        Set<ObjectDataWrapper> res = new LinkedHashSet<ObjectDataWrapper>();
        for (String json : jsonStr) {
            res.add(parse(preDefClassType, json));
        }
        return res;
    }


    /**
     * @return Map<Object, ObjectDataWrapper>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map parse(String preDefClassType, Map<Object, String> jsonStr) {
        //Map res = new LinkedHashMap();
        for (Map.Entry entry : jsonStr.entrySet()) {
            //res.put(entry.getKey(), parse(preDefClassType, (String) entry.getValue()));
            entry.setValue(parse(preDefClassType, (String) entry.getValue()));
        }
        //return res;
        return jsonStr;
    }


    public static void main2(String[] args) {
        String str1 = "{'class':'com.taobao.tae.common.demo.DemoData', 'name':'linxuan','number':5}";
        JsonParser parser = new JsonParser(str1);
        Object res = parser.parse();
        System.out.println(res + "(" + res.getClass().getName() + ")");
    }


    public static void main(String[] args) {
        String s =
                "{'id':'id0','demoData':{'name':'n0','number':10}, 'demoDataList':[{'name':'n1','number':11},{'name':'n2','number':12}], 'demoDataMap':{13:{'name':'n3','number':13},14:{'name':'n4','number':14}}}";
        JsonParser parser = new JsonParser(s);
        Object res = parser.parse();
        StringBuilder sb = new StringBuilder();
        JsonParser.toJsonStr(res, sb, "   ", 0, "'");
        System.out.println(sb);
    }

}
