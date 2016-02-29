package com.yxy.core.cache;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 用来将数据库数据进行缓存的cachemap-三级缓存
 * @author YongXinYu
 * @date 2015年8月5日 下午6:08:17
 * @param <K>
 * @param <V>
 */
public class CacheMap<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 1L;
	private transient Cache<K, V> cache = null;
	private transient DataProvide<K, V> provide = null;
	private static ExecutorService exec = Executors.newFixedThreadPool(4);

	public CacheMap(String name, DataProvide<K, V> provide, int ttl) {
		this.provide = provide;
		this.cache = new Cache<>();
		this.cache.setTtl(ttl);
		CacheMapManager.getInstance().addCache(name, this);
	}

	public CacheMap(String name) {
		this(name, null);
	}

	public CacheMap(String name, int ttl) {
		this(name, null, ttl);
	}

	public CacheMap(String name, DataProvide<K, V> provide) {
		this(name, provide, 300);
	}

	private void putCache(K key, V value) {
		if ((key == null) || (value == null))
			return;
		if (this.cache != null)
			this.cache.put(new Element<>(key, value));
	}

	private V getCache(Object key) {
		V ans = null;
		Element<K, V> element = this.cache.get(key);
		if (element != null) {
			ans = element.getValue();
			element.resetCreateTime();
		}
		return ans;
	}

	public int sizeCache() {
		int size = 0;
		if (this.cache != null) {
			size = this.cache.getSize();
		}
		return size;
	}

	protected V getWithCache(K key) {
		V value = get(key);
		if ((value == null) && (this.cache != null)) {
			value = getCache(key);
			if (value != null) {
				put(key, value);
			}
		}
		return value;
	}

	public V queryWithDB(K key) {
		if (key == null)
			return null;
		V value = get(key);
		if (value == null)
			value = getCache(key);
		if (value == null) {
			value = this.provide.get(key);
			putCache(key, value);
		}
		return value;
	}

	public V getWithCache(K key, boolean withDB) {
		V value = getWithCache(key);
		if ((value == null) && (withDB) && (this.provide != null)) {
			value = this.provide.get(key);
			if (value != null) {
				put(key, value);
			}
		}
		return value;
	}

	protected V putWithCache(K key, V value) {
		putCache(key, value);
		return put(key, value);
	}

	public V putWithCache(K key, V value, boolean withDB) {
		if (withDB) {
			this.provide.add(key, value);
		}
		putCache(key, value);
		return put(key, value);
	}

	private V removeWithCache(K key) {
		V value = remove(key);
		if ((value != null) && (this.cache != null)) {
			putCache(key, value);
		}
		return value;
	}

	public V removeWithCache(K key, boolean withDB) {
		V value = removeWithCache(key);
		if ((withDB) && (value != null) && (this.provide != null)) {
			UpdateTask<K, V> task = new UpdateTask<>(key, value, this.provide);
			exec.submit(task);
		}
		return value;
	}

	public Set<K> updateAll() {
		Set<K> ans = new HashSet<>();
		for (Entry<K, V> entry : entrySet()) {
			K key = entry.getKey();
			V value = entry.getValue();
			try {
				this.provide.update(key, value);
			} catch (Exception e) {
				ans.add(key);
				e.printStackTrace();
			}
		}
		return ans;
	}

	public void update(K key) {
		if (key == null)
			return;
		V value = get(key);
		if ((key != null) && (value != null) && (this.provide != null)) {
			UpdateTask<K, V> task = new UpdateTask<>(key, value, this.provide);
			exec.submit(task);
		}
	}

	public void update(K key, V value) {
		if ((key != null) && (value != null) && (this.provide != null))
			this.provide.update(key, value);
	}

	public Cache<K, V> getCache() {
		return this.cache;
	}

	public void setCache(Cache<K, V> cache) {
		this.cache = cache;
	}

	public DataProvide<K, V> getProvide() {
		return this.provide;
	}

	public void setProvide(DataProvide<K, V> provide) {
		this.provide = provide;
	}

	/**
	 * @Description: 更新数据至数据库线程
	 * @author YongXinYu
	 * @date 2015年8月5日 下午6:41:37
	 * @param <K>
	 * @param <V>
	 */
	static class UpdateTask<K, V> implements Runnable {
		private K key;
		private V value;
		private DataProvide<K, V> provide;

		public UpdateTask(K key, V value, DataProvide<K, V> provide) {
			this.key = key;
			this.value = value;
			this.provide = provide;
		}

		public void run() {
			try {
				this.provide.update(this.key, this.value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}