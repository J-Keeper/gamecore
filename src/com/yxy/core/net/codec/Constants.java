package com.yxy.core.net.codec;

import java.nio.ByteOrder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {
	public static final byte PROTOCOL_VERSION = 1;
	public static final int DEFAULT_BUFFER_CAPACITY = 32;
	public static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	/** 消息体分包最大字节数 */
	public static final int MAX_BODY_LEN = 65515;
	/** 是否打印日志 */
	public static boolean IS_SHOW_LOG = false;
	/** 是否是心跳协议 */
	public static boolean IS_HEART_BEAT = false;

	public static final Map<Short, Integer> freqMap = new ConcurrentHashMap<>();

	public static void setFreqMap(Short msgType, Integer freq) {
		freqMap.put(msgType, freq);
	}
}