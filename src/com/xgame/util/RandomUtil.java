package com.xgame.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {

	private final static ThreadLocal<SecureRandom> secureRandomThreadLocal = new ThreadLocal<SecureRandom>();

	public static Random getRandom() {
		SecureRandom random = secureRandomThreadLocal.get();
		if (random == null) {
			random = new SecureRandom();
			secureRandomThreadLocal.set(random);
		}
		return random;
	}

	/**
	 * 计算成功率
	 * 
	 * @param ratio
	 * @return boolean
	 * @throws
	 * @author YXY
	 */
	public static boolean calcSucRate(float ratio) {
		int ratioInt = (int) (ratio * 100);
		int nextInt = getRandom().nextInt(100) + 1;
		if (nextInt <= ratioInt) {
			return true;
		}
		return false;
	}
}
