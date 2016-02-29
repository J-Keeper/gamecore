package com.yxy.core.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 二级cache缓存，用于包装一级的element
 * @author YongXinYu
 * @date 2015年8月5日 下午5:47:18
 * @param <K>
 * @param <V>
 */
public class Cache<K, V> {
	private Map<Object, Element<K, V>> map;
	private long ttl;

	public Cache() {
		this.map = new ConcurrentHashMap<>();
	}

	public void put(Element<K, V> element) {
		this.map.put(element.getKey(), element);
	}

	public Element<K, V> get(Object key) {
		if (key == null) {
			return null;
		}
		Element<K, V> element = (Element<K, V>) this.map.get(key);
		return element;
	}

	public V remove(Object key) {
		if (key == null) {
			return null;
		}
		Element<K, V> element = (Element<K, V>) this.map.remove(key);
		if (element == null) {
			return null;
		}
		return element.getValue();
	}

	public long getTtl() {
		return this.ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl * 1000;
	}

	public int getSize() {
		return this.map.size();
	}

	public void clean() {
		Iterator<Element<K, V>> it = this.map.values().iterator();
		long curr = System.currentTimeMillis();
		while (it.hasNext()) {
			Element<K, V> element = (Element<K, V>) it.next();
			if ((curr - element.getCreateTime() > this.ttl)
					|| (element.getValue() == null)) {
				it.remove();
			}
		}
	}
}