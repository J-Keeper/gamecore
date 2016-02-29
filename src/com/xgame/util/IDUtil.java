package com.xgame.util;

import java.util.UUID;

/**
 * @author yaowenhao
 * @date 2014年6月12日 下午6:06:24
 */
public class IDUtil {
	public static String uuid() {
		return UUID.randomUUID().toString();
	}
}
