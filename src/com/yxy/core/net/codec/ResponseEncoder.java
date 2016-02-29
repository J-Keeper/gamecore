package com.yxy.core.net.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseEncoder implements MessageEncoder<Message> {
	private Logger log = LoggerFactory.getLogger(getClass());

	public void encode(IoSession session, Message message,
			ProtocolEncoderOutput out) throws Exception {
		IoBuffer msgBuf = compress(message);
		if (msgBuf.capacity() > 65515) {
			this.log.warn(
					"Message too long! type: {}  length: {} type: {}",
					new Object[] { Short.valueOf(message.getType()),
							Integer.valueOf(msgBuf.capacity()),
							msgBuf.toString() });
		}
		out.write(msgBuf);
	}

	private IoBuffer compress(Message msg) throws IOException {
		if (msg.encodeBody().length < 300) {
			return msg.build();
		}
		byte[] body = msg.encodeBody();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(out);
		try {
			dos.write(body);
		} finally {
			dos.close();
			out.close();
		}
		body = out.toByteArray();
		msg.getHeader().setVersion((byte) 5);
		IoBuffer msgBuf = BufUtil.getAutoExpandBuf(msg.getLength());
		msg.getHeader().setLength(body.length);
		msgBuf.put(msg.getHeader().encode());
		msgBuf.put(body);
		msgBuf.flip();
		return msgBuf;
	}
}
