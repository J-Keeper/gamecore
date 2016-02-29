package com.xgame.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象序列化和反序列工具类
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:36:49
 */
public class ObjectUtil {
	/**
	 * 将byte数组转为响应的对象(从数据库读取数据用)
	 * 
	 * @param array
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> clazz, byte[] array) {
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(array);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null)
				try {
					ois.close();
				} catch (IOException localIOException2) {
				}
			if (bis != null)
				try {
					bis.close();
				} catch (IOException localIOException3) {
				}
		}
		return (T) obj;
	}

	/**
	 * 将任意对象序列化为byte数组
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] fromObject(Object obj) {
		byte[] array = (byte[]) null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oop = null;
		try {
			bos = new ByteArrayOutputStream();
			oop = new ObjectOutputStream(bos);
			oop.writeObject(obj);
			array = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oop != null)
				try {
					oop.close();
				} catch (IOException localIOException2) {
				}
			if (bos != null)
				try {
					bos.close();
				} catch (IOException localIOException3) {
				}
		}
		return array;
	}

	/**
	 * 复制对象
	 * 
	 * @param clazz
	 * @param obj
	 * @return
	 */
	public static <T> T copy(Class<T> clazz, T obj) {
		byte[] array = fromObject(obj);
		return toObject(clazz, array);
	}
}