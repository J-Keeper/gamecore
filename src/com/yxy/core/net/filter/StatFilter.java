package com.yxy.core.net.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.yxy.core.net.codec.Message;

/**
 * @Description: 消息统计过滤器-针对每种协议的消息
 * @author YongXinYu
 * @date 2015年8月6日 下午7:32:59
 */
public class StatFilter extends IoFilterAdapter {
	public static Map<Short, MessageStat> statMap = new ConcurrentHashMap<>();

	/**
	 * 消息到达时的处理
	 */
	public void messageReceived(IoFilter.NextFilter nextFilter,
			IoSession session, Object message) throws Exception {
		Message msg = (Message) message;
		MessageStat stat = new MessageStat(msg.getType(), msg.getLength(), 1);
		MessageStat oldStat = (MessageStat) statMap.get(Short.valueOf(msg
				.getType()));
		if (oldStat == null) {
			statMap.put(Short.valueOf(msg.getType()), stat);
		} else {
			oldStat.add(stat);
		}
		nextFilter.messageReceived(session, message);
	}

	/**
	 * 消息发送时的处理
	 */
	public void messageSent(IoFilter.NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		Message msg = (Message) writeRequest.getMessage();
		MessageStat stat = new MessageStat(msg.getType(), msg.getLength(), 2);
		MessageStat oldStat = (MessageStat) statMap.get(Short.valueOf(msg
				.getType()));
		if (oldStat == null) {
			statMap.put(Short.valueOf(msg.getType()), stat);
		} else {
			oldStat.add(stat);
		}
		nextFilter.messageSent(session, writeRequest);
	}
}