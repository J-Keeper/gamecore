package com.yxy.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Strings;

public class ConfigBuilder {
	public static Object autowired(Properties prop, Class<?> clazz) {
		Object confObj = null;
		try {
			confObj = clazz.newInstance();

			Field[] fields = clazz.getDeclaredFields();
			Field[] fields2 = clazz.getFields();
			Set<Field> fieldSet = new HashSet<Field>();
			for (Field field : fields) {
				fieldSet.add(field);
			}
			for (Field field : fields2) {
				fieldSet.add(field);
			}
			for (Field field : fieldSet) {
				field.setAccessible(true);
				Config conf = field.getAnnotation(Config.class);
				if (conf == null) {
					continue;
				}
				String alias = conf.alias();
				String parser = conf.parser();
				boolean isParser = conf.allowNull();

				String finalFname = alias.equals("") ? field.getName() : alias;
				String propValue = prop.getProperty(finalFname);// prop.get(finalFname);
				if (propValue == null) {
					finalFname = finalFname.toLowerCase();
					propValue = prop.getProperty(finalFname);

					if (propValue == null && !isParser) {
						throw new Exception("config Class '" + clazz.getName()
								+ "'   config Field '" + finalFname
								+ "'  not find");
					}
				}
				setValue(confObj, field, parser, propValue, prop, clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return confObj;
	}

	private static void setValue(Object confObj, Field field, String parse,
			String filedValue, Properties prop, Class<?> clazz)
			throws Exception {
		Class<?> type = field.getType();
		if (type == Integer.TYPE || type == Integer.class) {
			if (Strings.isBlank(filedValue))
				filedValue = "0";
			field.set(confObj, Integer.parseInt(filedValue));
		} else if (type == Long.TYPE || type == Long.class) {
			field.set(confObj, Long.parseLong(filedValue));
		} else if (type == Boolean.TYPE || type == Boolean.class) {
			field.set(confObj, Boolean.parseBoolean(filedValue));
		} else if (type == Byte.TYPE || type == Byte.class) {
			field.set(confObj, Byte.parseByte(filedValue));
		} else if (type == Double.TYPE || type == Double.class) {
			field.set(confObj, Double.parseDouble(filedValue));
		} else if (type == Short.TYPE || type == Short.class) {
			field.set(confObj, Short.parseShort(filedValue));
		} else if (type == Float.TYPE || type == Float.class) {
			field.set(confObj, Float.parseFloat(filedValue));
		} else if (type == String.class) {
			field.set(confObj, filedValue);
		} else {
			if (Strings.isBlank(parse)) {
				throw new Exception(
						"不是short，int，float，double，long，String类型，必须在改类中自己编写并指定指定解析方法");
			}
			Method method = null;
			try {
				/** 针对自己解析Properties */
				method = clazz.getMethod(parse, Properties.class);
			} catch (Exception e) {

			}

			if (method == null) {
				method = clazz.getMethod(parse, String.class);
			}
			Class<?>[] paraTypes = method.getParameterTypes();
			Object obj;
			if (paraTypes[0] == PropertiesProxy.class) {
				obj = method.invoke(clazz.newInstance(), prop);
			} else {
				if (filedValue == null)
					return;
				obj = method.invoke(clazz.newInstance(), filedValue);
			}
			field.set(confObj, obj);
		}
	}
}