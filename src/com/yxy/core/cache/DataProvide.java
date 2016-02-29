package com.yxy.core.cache;

/**
 * @Description: 数据库DAO的简易封装
 * @author YongXinYu
 * @date 2015年8月5日 下午5:55:18
 * @param <K>
 * @param <V>
 */
public interface DataProvide<K, V> {

	public V get(K paramK);

	public void add(K paramK, V paramV);

	public void update(K paramK, V paramV);

	public void delete(K paramK);
}