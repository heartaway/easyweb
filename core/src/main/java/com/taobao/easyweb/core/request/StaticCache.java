package com.taobao.easyweb.core.request;

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
			if (!key.startsWith("file:")) {
				return new byte[0];
			}
			InputStream input = new FileInputStream(key.replace("file:", ""));
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
