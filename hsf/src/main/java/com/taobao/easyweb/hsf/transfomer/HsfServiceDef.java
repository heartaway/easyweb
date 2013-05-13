package com.taobao.easyweb.hsf.transfomer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Hsf服务元信息定义 类名、方法、参数等信息
 * 
 * @author linxuan
 * 
 */
public class HsfServiceDef {
	// private static final Logger log = LoggerUtil.getLogger();

	private String hsfServiceName;
	private String hsfServiceVersion;
	// methodName0:java.lang.String,java.lang.Integer
	// private Map<String/*methodName*/, String/*commaArgStringTypes*/>
	// method2argstypes = Collections.emptyMap();
	private Map<String, List<MethodSignatrue>> methodSignatrurs; // value出于处理方便，没必要用Set
	private ReturnTransformer returnTransformer;
	private ObjectDataWrapperParser objectDataWrapperParser;
	private Set<String> autoWrapperTypes = Collections.emptySet();
	private boolean isAutoAdaptNumber;

	public void setUniqueServiceName(String uniqueServiceName) {
		String[] hsfServiceNameVersion = uniqueServiceName.split(":");
		hsfServiceName = hsfServiceNameVersion[0];
		hsfServiceVersion = hsfServiceNameVersion[1];
	}

	/*
	 * public void setMethodSignatrurs(List<String> signatrures) { Map<String,
	 * String> signatruresMap = new HashMap<String, String>(); for (String
	 * signatrur : signatrures) { signatruresMap.put(signatrur, ""); }
	 * this.setMethodSignatrurs(signatruresMap); }
	 */

	/**
	 * 不支持这种情况：方法名相同，参数个数相同。同一个参数位置，一个类型是DemoData，一个类型是Map或者String；
	 * 因为无法精确区分一个Map或String倒底是普通的还是代表一个对象
	 */
	public void setMethodSignatrurs(Map<String, String> signatrures) {
		methodSignatrurs = new LinkedHashMap<String, List<MethodSignatrue>>(); // 用有序map
		for (Map.Entry<String, String> entry : signatrures.entrySet()) {
			String sig = entry.getKey();
			try {
				String[] nt = sig.split(":");
				String name = nt[0].trim(); // 方法名
				String types = nt[1].trim(); // 方法类型列表，逗号分隔
				String[] typeArray = types.split(","); // 方法类型数组
				for (int i = 0, n = typeArray.length; i < n; i++) {
					typeArray[i] = typeArray[i].trim();
				}
				MethodSignatrue ms = new MethodSignatrue(name, typeArray, entry.getValue().trim());
				List<MethodSignatrue> methods = methodSignatrurs.get(name); // 相同方法名的多个方法的类型数组（重载）
				if (methods == null) {
					methods = new LinkedList<MethodSignatrue>();
					methodSignatrurs.put(name, methods);
				}
				methods.add(ms);
			} catch (Exception e) {
				// log.fatal("parse methodSignatrurs failed. signatrur = " +
				// sig, e);
			}
		}
	}

	public void setAutoWrapperTypes(Set<String> types) {
		this.autoWrapperTypes = new HashSet<String>();
		for (String type : types) {
			autoWrapperTypes.add(type);
			autoWrapperTypes.add("[L" + type);
		}
	}

	public MethodSignatrue getParameterTypes(String methodName, Object[] args) {
		List<MethodSignatrue> methods/* 多个重载方法 */= methodSignatrurs.get(methodName);
		if (methods.size() == 1) {
			// 如果同一名称只有一个方法，没有重载，那么出于效率直接返回这个方法。
			return methods.get(0);
		}
		for (MethodSignatrue ms : methods) {
			if (isMatch(ms.paramTypes, args)) {
				return ms;
			}
		}
		return null;
	}

	public static Set<Class<?>> basicClasses;
	static {
		basicClasses = new HashSet<Class<?>>();
		basicClasses.add(Integer.class);
		basicClasses.add(Long.class);
		basicClasses.add(Boolean.class);
		basicClasses.add(Float.class);
		basicClasses.add(Double.class);
		basicClasses.add(Character.class);
		basicClasses.add(Byte.class);
		basicClasses.add(Short.class);

		basicClasses.add(int.class);
		basicClasses.add(long.class);
		basicClasses.add(boolean.class);
		basicClasses.add(float.class);
		basicClasses.add(double.class);
		basicClasses.add(char.class);
		basicClasses.add(byte.class);
		basicClasses.add(short.class);
	}

	private boolean isMatch(String[] types, Object[] args) {
		if (types.length != args.length) {
			return false;
		}
		for (int i = 0, n = types.length; i < n; i++) {
			if (!isMatch(types[i], args[i].getClass())) {
				if (!isAutoWrapperMatch(types[i], args[i])) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isMatch(String type, Class<?> argclass) {
		if (argclass == null) {
			return false;
		}
		if (type.equals(argclass.getName())) {
			return true;
		}
		if (isAutoboxingMatch(type, argclass)) {
			return true;
		}
		if (isAutoAdaptNumber(type, argclass)) {
			return true;
		}
		if (isMatch(type, argclass.getSuperclass())) {
			return true;
		}
		for (Class<?> intf : argclass.getInterfaces()) {
			if (isMatch(type, intf)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAutoboxingMatch(String type, Class<?> argclass) {
		if (argclass == Integer.class && int.class.getName().equals(type) //
				|| argclass == Long.class && long.class.getName().equals(type) //
				|| argclass == Boolean.class && boolean.class.getName().equals(type) //
				|| argclass == Float.class && float.class.getName().equals(type) //
				|| argclass == Double.class && double.class.getName().equals(type) //
				|| argclass == Character.class && char.class.getName().equals(type) //
				|| argclass == Byte.class && byte.class.getName().equals(type) //
				|| argclass == Short.class && short.class.getName().equals(type)) {
			return true;
		}
		return false;
	}

	public boolean isAutoAdaptNumber(String parameterType, Class<?> argclass) {
		// php经quercus解析后，数值类型默认为Long，所以这里暂时只处理Long到其他类型的转换
		if (isAutoAdaptNumber && argclass == Long.class && (int.class.getName().equals(parameterType) || short.class.getName().equals(parameterType))) {
			return true;
		}
		return false;
	}

	private boolean isAutoWrapperMatch(String parameterType, Object arg) {
		if (objectDataWrapperParser == null || autoWrapperTypes == null) {
			return false;
		}
		if (basicClasses.contains(arg.getClass())) {
			return false;
		}
		if (isAutoWrapper(parameterType, arg)) {
			return true;
		} else if (isAutoWrapperMapArray(parameterType, arg) != null) {
			return true;
		} else if (isAutoWrapperStringArray(parameterType, arg) != null) {
			return true;
		} else if (isAutoWrapperMapList(parameterType, arg) != null) {
			return true;
		} else if (isAutoWrapperStringList(parameterType, arg) != null) {
			return true;
		} else if (isAutoWrapperMapSet(parameterType, arg) != null) {
			return true;
		} else if (isAutoWrapperStringSet(parameterType, arg) != null) {
			return true;
		}
		/*
		 * else if (isAutoWrapperMap(parameterType, arg) != null) { return true;
		 * }
		 */
		return false;
	}

	public boolean isAutoWrapper(String parameterType, Object arg) {
		if (!autoWrapperTypes.contains(parameterType)) {
			// hsf方法签名中的parameterType必须在autoWrapperTypes有，才做其他类型比如string、map到签名类型的自动转换。
			return false;
		}
		if (arg.getClass() == String.class) {
			// 如果一个参数本身的类型是String，但为对应的接口方法参数指定的parameterType却不是String
			return true;
		}
		// if (Map.class.isAssignableFrom(arg.getClass()) &&
		// ObjectDataWrapperHelper.isObjectInMap((Map) arg)) {
		if (Map.class.isAssignableFrom(arg.getClass())) {
			return true;
		}
		return false;
	}

	public boolean isAutoWrapperString(String parameterType, Object arg) {
		if (!autoWrapperTypes.contains(parameterType)) {
			// hsf方法签名中的parameterType必须在autoWrapperTypes有，才做其他类型比如string、map到签名类型的自动转换。
			return false;
		}
		if (arg.getClass() == String.class) {
			// 如果一个参数本身的类型是String，但为对应的接口方法参数指定的parameterType却不是String
			return true;
		}
		return false;
	}

	public boolean isAutoWrapperDirectMap(String parameterType, Object arg) {
		if (!autoWrapperTypes.contains(parameterType)) {
			// hsf方法签名中的parameterType必须在autoWrapperTypes有，才做其他类型比如string、map到签名类型的自动转换。
			return false;
		}
		// if (Map.class.isAssignableFrom(arg.getClass()) &&
		// ObjectDataWrapperHelper.isObjectInMap((Map) arg)) {
		if (Map.class.isAssignableFrom(arg.getClass())) {
			// 如果一个参数本身的类型是Map，同时定义类型不是map，并且在autoWrapperTypes中，说明是Map-Object映射
			return true;
		}
		return false;
	}

	public String isAutoWrapperMapArray(String parameterType, Object arg) {
		if (arg.getClass() == Map[].class && parameterType.startsWith("[L") && parameterType.endsWith(";")) {
			String elementType = parameterType.substring(2, parameterType.length() - 1);
			if (autoWrapperTypes.contains(elementType)) {
				return elementType;
			}
		}
		return null;
	}

	public String isAutoWrapperStringArray(String parameterType, Object arg) {
		if (arg.getClass() == String[].class && parameterType.startsWith("[L") && parameterType.endsWith(";")) {
			String elementType = parameterType.substring(2, parameterType.length() - 1);
			if (autoWrapperTypes.contains(elementType)) {
				return elementType;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public String isAutoWrapperMapList(String parameterType, Object arg) {
		if (List.class.isAssignableFrom(arg.getClass()) && hasMapElement((List) arg) //
				&& parameterType.startsWith("java.util.List(") && parameterType.endsWith(")")) {
			String elementType = parameterType.substring("java.util.List(".length(), parameterType.length() - 1);
			if (autoWrapperTypes.contains(elementType)) {
				return elementType;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public String isAutoWrapperStringList(String parameterType, Object arg) {
		if (List.class.isAssignableFrom(arg.getClass()) && hasStringElement((List) arg) //
				&& parameterType.startsWith("java.util.List(") && parameterType.endsWith(")")) {
			String elementType = parameterType.substring("java.util.List(".length(), parameterType.length() - 1);
			if (autoWrapperTypes.contains(elementType)) {
				return elementType;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public String isAutoWrapperMapSet(String parameterType, Object arg) {
		if (Set.class.isAssignableFrom(arg.getClass()) && hasMapElement((Set) arg) //
				&& parameterType.startsWith("java.util.Set(") && parameterType.endsWith(")")) {
			String elementType = parameterType.substring("java.util.Set(".length(), parameterType.length() - 1);
			if (autoWrapperTypes.contains(elementType)) {
				return elementType;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public String isAutoWrapperStringSet(String parameterType, Object arg) {
		if (Set.class.isAssignableFrom(arg.getClass()) && hasStringElement((Set) arg) //
				&& parameterType.startsWith("java.util.Set(") && parameterType.endsWith(")")) {
			String elementType = parameterType.substring("java.util.Set(".length(), parameterType.length() - 1);
			if (autoWrapperTypes.contains(elementType)) {
				return elementType;
			}
		}
		return null;
	}

	/*
	 * @SuppressWarnings("rawtypes") public String isAutoWrapperMap(String
	 * parameterType, Object arg) { if
	 * (Map.class.isAssignableFrom(arg.getClass()) && hasStringElement((Map)
	 * arg)// && parameterType.startsWith("java.util.Map(") &&
	 * parameterType.endsWith(")")) { String elementType =
	 * parameterType.substring("java.util.Map(".length(), parameterType.length()
	 * - 1); if (autoWrapperTypes.contains(elementType)) { return elementType; }
	 * } return null; }
	 */

	public static boolean hasStringElement(Collection<?> c) {
		if (c.isEmpty()) {
			return false;
		}
		for (Object obj : c) {
			if (obj != null && obj.getClass() == String.class) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasMapElement(Collection<?> c) {
		if (c.isEmpty()) {
			return false;
		}
		for (Object obj : c) {
			if (obj != null && obj instanceof Map) {
				// if (ObjectDataWrapperHelper.isObjectInMap((Map) obj)) {
				return true;
				// }
			}
		}
		return false;
	}

	public static boolean hasStringElement(Map<?, ?> map) {
		if (map.isEmpty()) {
			return false;
		}
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (entry.getValue() != null && entry.getValue().getClass() == String.class) {
				return true;
			}
		}
		return false;
	}

	public static class MethodSignatrue {
		public final String methodName;
		public final String[] paramTypes;
		public final String returnType;

		// private String[] throwTypes;
		public MethodSignatrue(String methodName, String[] paramTypes, String returnType) {
			this.methodName = methodName;
			this.paramTypes = paramTypes;
			this.returnType = returnType;
		}
	}

	public static interface ReturnTransformer {
		Object transformer(String hsfReturnType, Object hsfReturnObject);
	}

	public String getHsfServiceName() {
		return hsfServiceName;
	}

	public String getHsfServiceVersion() {
		return hsfServiceVersion;
	}

	public ObjectDataWrapperParser getObjectDataWrapperParser() {
		return objectDataWrapperParser;
	}

	public void setObjectDataWrapperParser(ObjectDataWrapperParser objectDataWrapperParser) {
		this.objectDataWrapperParser = objectDataWrapperParser;
	}

	public Set<String> getAutoWrapperTypes() {
		return autoWrapperTypes;
	}

	public boolean isAutoAdaptNumber() {
		return isAutoAdaptNumber;
	}

	public void setAutoAdaptNumber(boolean isAutoAdaptNumber) {
		this.isAutoAdaptNumber = isAutoAdaptNumber;
	}

	public ReturnTransformer getReturnTransformer() {
		return returnTransformer;
	}

	public void setReturnTransformer(ReturnTransformer returnTransformer) {
		this.returnTransformer = returnTransformer;
	}
}