package com.taobao.easyweb.hsf;

import java.util.concurrent.ConcurrentHashMap;

import com.taobao.hsf.ndi.SpringConsumerBeanNDI;

/**
 */
public class HSFInvokeHelper {

	private static ConcurrentHashMap<String, SpringConsumerBeanNDI> hsfConsumers = new ConcurrentHashMap<String, SpringConsumerBeanNDI>();
	public static Object invokeHsfService(String serviceName, String version, String methodName, String[] parameterTypes, Object args[]) throws Throwable {
		String hsfserviceId = serviceName + ":" + version;
		SpringConsumerBeanNDI ndi = hsfConsumers.get(hsfserviceId);
		if (ndi == null) {
			ndi = new SpringConsumerBeanNDI();
			ndi.setInterfaceName(serviceName);
			ndi.setVersion(version);
			ndi.init();
			SpringConsumerBeanNDI old = hsfConsumers.putIfAbsent(hsfserviceId, ndi);
			if (old != null) {
				ndi = old;
			}
		}
		Object res = ndi.invoke(methodName, parameterTypes, args);
		return res;
	}
}
