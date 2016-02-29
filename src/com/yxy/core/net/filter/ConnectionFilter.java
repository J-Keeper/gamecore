package com.yxy.core.net.filter;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.yxy.core.net.codec.Message;

public class ConnectionFilter extends IoFilterAdapter {
	private int maxConnections;
	private AtomicInteger connectedCount = new AtomicInteger(0);

	public ConnectionFilter(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session)
			throws Exception {
		if (this.connectedCount.incrementAndGet() > this.maxConnections) {
			if (session != null) {
				Message msg = new Message((short) -99);
				session.write(msg);
				session.close(true);
			}
		} else {
			nextFilter.sessionCreated(session);
		}
	}

	/**
	 * 关闭连接
	 */
	public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session)
			throws Exception {
		this.connectedCount.decrementAndGet();
		nextFilter.sessionClosed(session);
	}

	public void messageReceived(IoFilter.NextFilter nextFilter,
			IoSession session, Object message) throws Exception {
		Message msg = (Message) message;
		msg.setSession(session);
		Object obj = session.getAttribute("attrib_isLogin");
		boolean isLogin = obj == null ? false : ((Boolean) obj).booleanValue();
		if ((isLogin) || (102 == msg.getType()) || (100 == msg.getType())) {
			nextFilter.messageReceived(session, message);
		} else {
			session.close(true);
		}
	}

	/**
	 * 设置session的状态
	 */
	public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {
		Object obj = session.getAttribute("attrib_isLogin");
		boolean isLogin = obj == null ? false : ((Boolean) obj).booleanValue();
		// 如果处于离线-未登录状态，直接将Session关闭,否则自己设定一个状态
		if (!isLogin) {
			session.close(true);
		} else {
			nextFilter.sessionIdle(session, status);
		}
	}
}
