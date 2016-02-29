package com.yxy.core.net.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDecoder implements MessageDecoder {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final String DECODE_STATE = RequestDecoder.class.getName()
			+ ".CAFEBABE";

	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		in.order(Constants.DEFAULT_BYTE_ORDER);
		if (in.remaining() < Message.Header.SIZE) {
			return MessageDecoderResult.NEED_DATA;
		}
		return MessageDecoderResult.OK;
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		Message msg = (Message) session.getAttribute(DECODE_STATE);
		if (msg == null) {
			msg = new Message(session);
			msg.getHeader().decode(in);
			int bodyLen = msg.getHeader().getLength();
			if ((bodyLen < 0) || (bodyLen > 65515)) {
				this.log.error("Error body {} {}", msg.getHeader(),
						session.getRemoteAddress());
				return MessageDecoderResult.NOT_OK;
			}
			session.setAttribute(DECODE_STATE, msg);
		}
		int hLen = msg.getHeader().getLength();
		if (in.remaining() < hLen) {
			return MessageDecoderResult.NEED_DATA;
		}
		if (hLen > 0) {
			byte[] body = new byte[hLen];
			in.get(body);
			if (msg.getHeader().getVersion() == 5) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ByteArrayInputStream bis = new ByteArrayInputStream(body);
				InflaterInputStream iis = new InflaterInputStream(bis);
				try {
					byte[] buffer = new byte[64];
					int n;
					while ((n = iis.read(buffer)) >= 0) {
						bos.write(buffer, 0, n);
					}
				} finally {
					iis.close();
					bis.close();
					bos.close();
				}
				body = bos.toByteArray();
			}
			msg.setBodyBytes(body);
		}
		out.write(msg);
		session.setAttribute(DECODE_STATE, null);
		return MessageDecoderResult.OK;
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
		this.log.info("finishDecode:" + session.getRemoteAddress().toString());
	}
}