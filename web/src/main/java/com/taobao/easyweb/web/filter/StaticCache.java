package com.taobao.easyweb.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.taobao.easyweb.core.Configuration;

public class StaticCache {

	public static LoadingCache<String, byte[]> staticCaches = CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(Configuration.getStaticCacheTime(), TimeUnit.SECONDS).build(new CacheLoader<String, byte[]>() {
		public byte[] load(String key) throws Exception {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			if (key.startsWith("/")) {
				key = key.substring(1);
			}
			InputStream input = StaticCache.class.getClassLoader().getResourceAsStream(key);
			if (input == null) {
				try {
					if(key.startsWith("file:")){
						input = new FileInputStream(key.replace("file:", ""));
					}else{
//						input = new FileInputStream(Configuration.getStaticFilePath() + key);
					}
					
				} catch (Exception e) {
				}
			}
			if (input == null) {
				return new byte[0];// 如果找不到，则缓存一个空的
			}
			IOUtils.copy(input, stream);
			return stream.toByteArray();
		}
	});

	public static byte[] get(String uri) throws IOException {
		try {
			return staticCaches.get(uri);
		} catch (ExecutionException e) {
			throw new IOException(e);
		}
	}
}
