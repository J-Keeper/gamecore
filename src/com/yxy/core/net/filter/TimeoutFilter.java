package com.yxy.core.net.filter;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

import com.yxy.core.net.codec.Message;

/**
 * @Description: 连接超时处理
 * @author YongXinYu
 * @date 2015年8月6日 下午10:10:35
 */
public class TimeoutFilter extends KeepAliveFilter {
	private static final int INTERVAL = 60;
	private static final int TIMEOUT = 35;

	public TimeoutFilter(KeepAliveMessageFactory messageFactory) {
		super(messageFactory);
	}

	/**
	 * 采用mina自带的超时过滤器,检测时间60s,超时时间35s
	 */
	public TimeoutFilter() {
		super(new KeepAliveMessageImple(), IdleStatus.BOTH_IDLE,
				KeepAliveRequestTimeoutHandler.CLOSE, INTERVAL, TIMEOUT);
		setForwardEvent(false);
	}

	/**
	 * 心跳超时管道
	 * 
	 * @Description: TODO
	 * @author YongXinYu
	 * @date 2015年8月6日 下午10:17:28
	 */
	static class KeepAliveMessageImple implements KeepAliveMessageFactory {
		/** 协议号 */
		private static final short MSG_REQ = 1;
		private static final short MSG_RES = -1;

		/** 服务器请求 */
		public Object getRequest(IoSession session) {
			Message msg = new Message();
			msg.setSession(session);
			msg.setType(MSG_REQ);
			return msg;
		}

		/** 客户端应答 */
		public Object getResponse(IoSession session, Object obj) {
			Message msg = new Message();
			msg.setSession(session);
			msg.setType(MSG_RES);
			return msg;
		}

		/**
		 * 是否是心跳请求
		 */
		public boolean isRequest(IoSession session, Object obj) {
			Message msg = (Message) obj;
			boolean ans = false;
			if ((msg != null) && (MSG_REQ == msg.getType())) {
				ans = true;
			}
			return ans;
		}

		public boolean isResponse(IoSession arg0, Object obj) {
			Message msg = (Message) obj;
			boolean ans = false;
			if ((msg != null) && (MSG_RES == msg.getType())) {
				ans = true;
			}
			return ans;
		}
	}
}