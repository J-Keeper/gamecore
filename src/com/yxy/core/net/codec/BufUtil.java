package com.yxy.core.net.codec;

import org.apache.mina.core.buffer.IoBuffer;

public class BufUtil {
	public static IoBuffer getAutoExpandBuf() {
		return getAutoExpandBuf(32);
	}

	public static IoBuffer getAutoExpandBuf(int initCapacity) {
		IoBuffer body = IoBuffer.allocate(initCapacity);
		body.order(Constants.DEFAULT_BYTE_ORDER);
		body.setAutoExpand(true);
		return body;
	}

	protected static byte[] getBufBytes(IoBuffer src) {
		byte[] b = new byte[src.position()];
		src.flip();
		src.get(b);
		return b;
	}
}