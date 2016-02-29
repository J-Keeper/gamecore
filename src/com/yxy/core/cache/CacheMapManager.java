package com.yxy.core.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:缓存cacheMap管理对象
 * @author YongXinYu
 * @date 2015年8月5日 下午6:49:34
 */
public class CacheMapManager {
	private static CacheMapManager instance = new CacheMapManager();
	private Map<String, CacheMap<?, ?>> cacheMaps;
	private CacheCleaner cleaner;

	private CacheMapManager() {
		this.cacheMaps = new HashMap<>();
		this.cleaner = new CacheCleaner(30000L);
		this.cleaner.start();
	}

	public static CacheMapManager getInstance() {
		return instance;
	}

	public void addCache(String cacheId, CacheMap<?, ?> cacheMap) {
		this.cacheMaps.put(cacheId, cacheMap);
	}

	public CacheMap<?, ?> getCache(String cacheId) {
		return (CacheMap<?, ?>) this.cacheMaps.get(cacheId);
	}

	public Map<String, CacheMap<?, ?>> getCacheMaps() {
		return this.cacheMaps;
	}
}