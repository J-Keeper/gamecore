package com.yxy.core.cache;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description: 缓存的元素-以键值对的形式存在，一级缓存
 * @author YongXinYu
 * @date 2015年8月5日 下午5:47:43
 * @param <K>
 * @param <V>
 */
public class Element<K, V> {
	private K key;
	private SoftReference<V> value;
	private AtomicLong createTime;

	public Element(K key, V value) {
		this.key = key;
		this.value = new SoftReference<>(value);
		this.createTime = new AtomicLong(System.currentTimeMillis());
	}

	public K getKey() {
		return this.key;
	}

	public V getValue() {
		if (this.value == null) {
			return null;
		}
		return this.value.get();
	}

	public long getCreateTime() {
		return this.createTime.get();
	}

	protected void resetCreateTime() {
		this.createTime.set(System.currentTimeMillis());
	}
}