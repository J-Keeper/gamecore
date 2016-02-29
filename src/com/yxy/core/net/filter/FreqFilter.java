package com.yxy.core.net.filter;

import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.net.codec.Message;

/**
 * @Description: 请求频率过滤
 * @author YongXinYu
 * @date 2015年8月6日 下午7:25:03
 */
public class FreqFilter extends IoFilterAdapter {
	private Logger log = LoggerFactory.getLogger(getClass());
	/** 频率过滤表，key-消息类型,value-间隔时间 */
	private Map<Short, Integer> freqMap;

	/**
	 * 初始化频率过滤器
	 * 
	 * @param freqMap
	 */
	public FreqFilter(Map<Short, Integer> freqMap) {
		this.freqMap = freqMap;
	}

	/**
	 * 消息达到后的处理
	 */
	public void messageReceived(IoFilter.NextFilter nextFilter,
			IoSession session, Object message) throws Exception {
		// 没有设置过滤，直接接受
		if (freqMap == null) {
			nextFilter.messageReceived(session, message);
			return;
		}
		Message msg = (Message) message;
		short type = msg.getType();
		if (freqMap.containsKey(Short.valueOf(type))) {
			String key = "msgFreq_" + type;
			long nowTime = System.currentTimeMillis();
			Object obj = session.getAttribute(key);
			// 初次接受消息
			if (obj == null) {
				session.setAttribute(key, Integer.valueOf(0));
				obj = Long.valueOf(0L);
			}
			long lastReq = ((Long) obj).longValue();
			int freq = ((Integer) this.freqMap.get(Short.valueOf(type)))
					.intValue();
			long interval = nowTime - lastReq;
			if (interval < freq) {
				log.warn(String.format(
						"MSG type=%s,请求频率过高,间隔 TIME=%sms < %sms %s",
						new Object[] { Short.valueOf(type),
								Long.valueOf(interval), Integer.valueOf(freq),
								msg }));
			} else {
				session.setAttribute(key, Long.valueOf(nowTime));
				nextFilter.messageReceived(session, message);
			}
		} else {
			nextFilter.messageReceived(session, message);
		}
	}
}