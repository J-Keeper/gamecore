package com.yxy.core.msg;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.net.codec.Message;

public class MessageHandler extends IoHandlerAdapter {
	private Logger log = LoggerFactory.getLogger(MessageHandler.class);
	private MessageDispatcher dispatcher;

	public MessageHandler(MessageDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		session.getConfig().setBothIdleTime(30);
	}

	public void sessionClosed(IoSession session) throws Exception {
		Object accountObj = session.getAttribute("attrib_username");
		if (accountObj != null) {
			Message msg = new Message(session);
			msg.setType((short) MessageConstant.LOGOUT_REQ);
			dispatcher.addInner(msg);
		}
		super.sessionClosed(session);
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		super.messageReceived(session, message);
		Message msg = (Message) message;
		msg.setSession(session);
		dispatcher.add(msg);
	}

	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.warn(cause.getClass() + " " + cause.getMessage() + " "
				+ session.toString());
	}
}