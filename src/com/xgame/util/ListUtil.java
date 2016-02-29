package com.xgame.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xgame.packet.MessageSerializable;
import com.yxy.core.util.Pair;

/**
 * list工具类
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:37:21
 */
public class ListUtil {

	public static List<Object> toMsg(
			Collection<? extends MessageSerializable> collection) {
		List<Object> list = new ArrayList<>();
		if (collection == null) {
			return list;
		}
		for (MessageSerializable ms : collection) {
			list.add(ms.toMsg());
		}
		return list;
	}

	public static List<Object> toMsg(Map<Integer, Integer> map) {
		List<Object> list = new ArrayList<>();
		if (map == null) {
			return list;
		}
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			Map<String, Integer> temp = new HashMap<>();
			temp.put("Key", entry.getKey());
			temp.put("Value", entry.getValue());
			list.add(temp);
		}
		return list;
	}

	public static List<Object> toMsg(List<Pair<Integer, Integer>> pairList) {
		List<Object> list = new ArrayList<>();
		for (Pair<Integer, Integer> pair : pairList) {
			Map<String, Integer> temp = new HashMap<>();
			temp.put("Key", pair.getKey());
			temp.put("Value", pair.getValue());
			list.add(temp);
		}
		return list;
	}

	/**
	 * 物品组加倍
	 * 
	 * @param list
	 * @param x
	 * @return List<Pair<Integer,Integer>>
	 * @throws
	 * @author YXY
	 */
	public static List<Pair<Integer, Integer>> getXList(
			List<Pair<Integer, Integer>> list, int x) {
		List<Pair<Integer, Integer>> xList = new ArrayList<>();
		for (Pair<Integer, Integer> pair : list) {
			int num = pair.getValue();
			xList.add(new Pair<>(pair.getKey(), num * x));
		}
		return xList;
	}

	/**
	 * 转成新的物品
	 * 
	 * @param list
	 * @return List<Pair<Integer,Integer>>
	 * @throws
	 * @author YXY
	 */
	public static List<Pair<Integer, Integer>> getCopyList(
			List<Pair<Integer, Integer>> list) {
		List<Pair<Integer, Integer>> xList = new ArrayList<>();
		if (list == null) {
			return xList;
		}
		for (Pair<Integer, Integer> pair : list) {
			int num = pair.getValue();
			xList.add(new Pair<>(pair.getKey(), num));
		}
		return xList;
	}

	/**
	 * 把有重复的物品叠加起来
	 * 
	 * @param pairs
	 * @return List<Pair<Integer,Integer>>
	 * @throws
	 * @author YXY
	 */
	public static List<Pair<Integer, Integer>> sumListItem(
			List<Pair<Integer, Integer>> pairs) {
		List<Pair<Integer, Integer>> pairList = new ArrayList<>();
		if (pairs == null || pairs.size() <= 0) {
			return pairList;
		}
		Map<Integer, Pair<Integer, Integer>> totalItem = new HashMap<>();
		for (Pair<Integer, Integer> pair : pairs) {
			if (pair.getValue() <= 0)
				continue;
			if (totalItem.containsKey(pair.getKey())) {
				int value = pair.getValue()
						+ totalItem.get(pair.getKey()).getValue();
				Pair<Integer, Integer> newPair = new Pair<>(pair.getKey(),
						value);
				totalItem.put(pair.getKey(), newPair);
			} else {
				Pair<Integer, Integer> newPair = new Pair<>(pair.getKey(),
						pair.getValue());
				totalItem.put(pair.getKey(), newPair);
			}
		}
		pairList.addAll(totalItem.values());
		return pairList;
	}

	/**
	 * 获取一组道具的数量和
	 * 
	 * @param list
	 * @return
	 * @since 2015年5月21日 下午7:07:47
	 * @author Yongxinyu
	 */
	public static int getResAmount(List<Pair<Integer, Integer>> list) {
		int sum = 0;
		for (Pair<Integer, Integer> pair : list) {
			sum += pair.getValue();
		}
		return sum;
	}

	/**
	 * Key-value对象列表转换成Message格式
	 * 
	 * @param pairs
	 * @return List<Object>
	 * @throws
	 * @author YXY
	 */
	public static List<Object> pairs2Msg(List<Pair<Integer, Integer>> pairs) {
		List<Object> msg = new ArrayList<>();
		if (pairs == null) {
			return msg;
		}
		for (Pair<Integer, Integer> pair : pairs) {
			Map<String, Integer> map = new HashMap<>();
			map.put("k", pair.getKey());
			map.put("v", pair.getValue());
			msg.add(map);
		}
		return msg;
	}

}
