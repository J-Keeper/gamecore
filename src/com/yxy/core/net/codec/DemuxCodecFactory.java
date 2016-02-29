package com.yxy.core.net.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class DemuxCodecFactory extends DemuxingProtocolCodecFactory {
	public DemuxCodecFactory() {
		super.addMessageDecoder(RequestDecoder.class);
		super.addMessageEncoder(Message.class, ResponseEncoder.class);
	}

}