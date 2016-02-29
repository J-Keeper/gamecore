package com.xgame.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Strings;

import com.xgame.conf.KeyValueConf;
import com.yxy.core.util.Config;
import com.yxy.core.util.Pair;

/**
 * 配置工具类
 */
public class ConfUtil {

	/**
	 * 将物品数量字符串转化为List<Pair<String, Integer>> List<<物品Id,数量>>
	 * 
	 * @param confString
	 * @return
	 */
	public static List<Pair<Integer, Integer>> toPairList(String confString) {
		List<Pair<Integer, Integer>> pairList = new ArrayList<>();
		if (Strings.isBlank(confString)) {
			return pairList;
		}
		String[] pairStrings = confString.split(";");
		for (String pairString : pairStrings) {
			String[] pairIds = pairString.split(":");
			Pair<Integer, Integer> pair = new Pair<>(
					Integer.parseInt(pairIds[0]), Integer.parseInt(pairIds[1]));
			pairList.add(pair);
		}
		return pairList;
	}

	/**
	 * 按比率计算物品
	 * 
	 * @param pairs
	 * @param ratio
	 * @return
	 * @since 2015年3月23日 下午12:34:05
	 * @author LSQ
	 */
	public static List<Pair<Integer, Integer>> calcPairsForRatio(
			List<Pair<Integer, Integer>> pairs, float ratio) {
		List<Pair<Integer, Integer>> newPairs = new ArrayList<Pair<Integer, Integer>>();
		if (pairs == null) {
			return newPairs;
		}
		List<Pair<Integer, Integer>> sumListItem = ListUtil.sumListItem(pairs);
		for (Pair<Integer, Integer> pair : sumListItem) {
			pair.setValue((int) (pair.getValue() * ratio));
			newPairs.add(pair);
		}
		return newPairs;
	}

	/**
	 * 转化为Pair
	 * 
	 * @data 2014年10月16日 下午3:57:26
	 * @param confString
	 * @return
	 */
	public static Pair<Integer, Integer> toPair(String confString) {
		String[] pairIds = confString.split(":");
		Pair<Integer, Integer> pair = new Pair<>(Integer.parseInt(pairIds[0]),
				Integer.parseInt(pairIds[1]));
		return pair;
	}

	/**
	 * 将怪物组字符串转化成List
	 * 
	 * @param confString
	 * @return
	 */
	public static List<String> toMonsterList(String confString) {
		List<String> monsterList = new ArrayList<String>();
		String[] monsterIds = confString.split(";");
		for (String monsterId : monsterIds) {
			monsterList.add(monsterId);
		}
		return monsterList;
	}

	/**
	 * 将PropertiesProxy转换成相应的配置类classPath
	 * 
	 * @data 2014年10月16日 下午6:48:36
	 * @param prop
	 * @param classPath
	 */
	public static void change(PropertiesProxy prop, String classPath) {
		try {
			/** 获取配置类 **/
			Class<?> paramSetClass = Class.forName(classPath);
			KeyValueConf avatarInit = (KeyValueConf) paramSetClass
					.newInstance();
			/** 设置配置字段值 **/
			Field[] fields = paramSetClass.getDeclaredFields();
			o: for (Field field : fields) {
				field.setAccessible(true);
				boolean hasAnnotation = field.isAnnotationPresent(Config.class);

				Class<?> filedType = field.getType();
				String filedTypeStr = filedType.getName();

				if (hasAnnotation) {
					/** 获取注解 **/
					Config keyValueAnnotation = field
							.getAnnotation(Config.class);
					if (keyValueAnnotation == null) {
						continue o;
					}
					/** 注解参数 **/
					String filedName = keyValueAnnotation.alias();
					String parseMethodName = keyValueAnnotation.parser();
					/** 获取配置值 **/
					String filedValue = getFieldValue(prop, field, filedName);
					try {
						/** 设置注解字段值 **/
						setValue(avatarInit, field, filedTypeStr,
								parseMethodName, filedValue);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getFieldValue(PropertiesProxy prop, Field field,
			String filedName) throws Exception {
		String filedValue = null;
		if (filedName.equals("")) {
			filedValue = prop.get(field.getName());
		} else {
			filedValue = prop.get(filedName);
		}
		if (filedValue == null) {
			throw new Exception("config Field '" + field.getName()
					+ "'  not find");
		}
		return filedValue;
	}

	/**
	 * 设置值
	 * 
	 * @data 2014年10月15日 下午5:09:09
	 * @param paramSetClass
	 * @param avatarInit
	 * @param field
	 * @param filedTypeStr
	 * @param parseMethodName
	 * @param filedValue
	 * @throws Exception
	 */
	private static void setValue(KeyValueConf avatarInit, Field field,
			String filedTypeStr, String parseMethodName, String filedValue)
			throws Exception {
		/** 普通类型 **/
		switch (filedTypeStr) {
		case "short":
			field.set(avatarInit, Short.parseShort(filedValue));
			break;
		case "int":
			field.set(avatarInit, Integer.parseInt(filedValue));
			break;
		case "float":
			field.set(avatarInit, Float.parseFloat(filedValue));
			break;
		case "double":
			field.set(avatarInit, Double.parseDouble(filedValue));
			break;
		case "long":
			field.set(avatarInit, Long.parseLong(filedValue));
			break;
		case "boolean":
			field.set(avatarInit, Boolean.parseBoolean(filedValue));
			break;
		case "java.lang.String":
			field.set(avatarInit, filedValue);
			break;
		/** 针对对象类型,如：map,list等 **/
		default:
			if (parseMethodName.equals("")) {
				throw new Exception(
						"不是short，int，float，double，long，String类型，必须指定解析方法");
			}
			Method method = avatarInit.getClass().getMethod(parseMethodName,
					String.class);
			Object obj = method.invoke(avatarInit, filedValue);
			field.set(avatarInit, obj);
			break;
		}
	}

	/**
	 * 合并List<Pair<String, Integer>>中Pair的key相同Pair
	 * 
	 * @param list
	 *            要合并的对象
	 * @return
	 */
	public static List<Pair<String, Integer>> combinePair(
			List<Pair<String, Integer>> list) {
		List<Pair<String, Integer>> tempList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null) {
				list.remove(i);
			}
		}
		Map<String, Integer> tempMap = new HashMap<>();
		for (Pair<String, Integer> entry : list) {
			if (tempMap.containsKey(entry.getKey())) {
				tempMap.put(entry.getKey(),
						tempMap.get(entry.getKey()) + entry.getValue());
			} else {
				tempMap.put(entry.getKey(), entry.getValue());
			}
		}
		for (Entry<String, Integer> e : tempMap.entrySet()) {
			tempList.add(new Pair<String, Integer>(e.getKey(), e.getValue()));
		}
		return tempList;
	}
}
