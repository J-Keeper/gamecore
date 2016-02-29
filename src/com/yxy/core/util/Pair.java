package com.yxy.core.util;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {
	private static final long serialVersionUID = 1L;
	private K key;
	private V value;

	public Pair() {
	}

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return this.key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public String toString() {
		return "Pair [key=" + this.key + ", value=" + this.value + "]";
	}
}
