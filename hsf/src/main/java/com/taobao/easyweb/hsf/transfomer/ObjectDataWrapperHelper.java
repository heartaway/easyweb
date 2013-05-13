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
public class ObjectDataWrapperHelper {
    private static final Log log = LogFactory.getLog(ObjectDataWrapperHelper.class);
    public static final String class_name_key = "class"; //java 关键字，不可能是对象属性


    @SuppressWarnings("rawtypes")
    public static boolean isObjectInMap(Map objmap) {
        String className = (String) objmap.get(class_name_key);
        return className != null && (className.startsWith("com.") || className.startsWith("org."));
    }


    public static List<ObjectDataWrapper> toObjectDataWrapper(String preDefClassName,
            List<Map<String, Object>> objmaplist) {
        List<ObjectDataWrapper> res = new LinkedList<ObjectDataWrapper>();
        for (Map<String, Object> objmap : objmaplist) {
            res.add(toObjectDataWrapper(preDefClassName, objmap));
        }
        return res;
    }


    public static ObjectDataWrapper[] toObjectDataWrapper(String preDefClassType, Map<String, Object>[] objmapArray) {
        ObjectDataWrapper[] res = new ObjectDataWrapper[objmapArray.length];
        for (int i = 0; i < objmapArray.length; i++) {
            res[i] = toObjectDataWrapper(preDefClassType, objmapArray[i]);
        }
        return res;
    }


    public static Set<ObjectDataWrapper> toObjectDataWrapper(String preDefClassType, Set<Map<String, Object>> objmapSet) {
        Set<ObjectDataWrapper> res = new LinkedHashSet<ObjectDataWrapper>();
        for (Map<String, Object> json : objmapSet) {
            res.add(toObjectDataWrapper(preDefClassType, json));
        }
        return res;
    }


    /**
     * @return Map<Object, ObjectDataWrapper>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map toObjectDataWrapperMap(String preDefClassType, Map<Object, Map<String, Object>> jsonStr) {
        //Map res = new LinkedHashMap();
        for (Map.Entry entry : jsonStr.entrySet()) {
            //res.put(entry.getKey(), parse(preDefClassType, (String) entry.getValue()));
            entry.setValue(toObjectDataWrapper(preDefClassType, (Map<String, Object>) entry.getValue()));
        }
        //return res;
        return jsonStr;
    }


    public static ObjectDataWrapper toObjectDataWrapper(String preDefClassName, Map<String, Object> objmap) {
        objmap.put(class_name_key, preDefClassName);
        return toObjectDataWrapper(objmap);
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ObjectDataWrapper toObjectDataWrapper(Map<String, Object> objmap) {
        log.debug("[toObjectDataWrapper]objmap:" + objmap);
        String className = (String) objmap.get(class_name_key);
        if (className == null || className.trim().length() == 0) {
            throw new IllegalArgumentException(
                "The map representing a object must have a 'class' entry to indicate the java type");
        }
        ObjectDataWrapper wrapper = new ObjectDataWrapper(className);

        for (Map.Entry<String, Object> e : objmap.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();
            if (class_name_key.equals(key)) {
                continue;
            }
            if (value instanceof Map) {
                wrapper.set(key, parseMap((Map) value));
            }
            else if (value instanceof List) {
                List list = (List) value;
                if (hasMapElement(list)) {
                    List replaceList = new LinkedList();
                    for (Object map : list) {
                        replaceList.add(parseMap((Map) map));
                    }
                    wrapper.set(key, replaceList);
                }
                else {
                    wrapper.set(key, list);
                }

            }
            else {
                wrapper.set(key, value);
            }
        }

        return wrapper;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object parseMap(Map objmap) {
        String className = (String) objmap.get(class_name_key);
        if (className == null) {
            //普通的Map
            for (Map.Entry entry : (Set<Map.Entry>) objmap.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    Map value = (Map) entry.getValue();
                    entry.setValue(parseMap(value));
                }
            }
            return objmap;
        }
        else {
            return toObjectDataWrapper(className, (Map<String, Object>) objmap);
        }
    }


    private static boolean hasMapElement(List<?> list) {
        if (list == null) {
            return false;
        }
        for (Object obj : list) {
            if (obj != null && obj instanceof Map) {
                return true;
            }
        }
        return false;
    }

}
