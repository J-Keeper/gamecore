package com.yxy.core.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.msg.adp.AdaptorException;
import com.yxy.core.net.codec.Message;

public final class MessageFacadeInvoker implements Runnable {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final int TIME_COST_MAX = 50;
	private final Message msg;
	private final MessageCallBack callback;

	public MessageFacadeInvoker(Message msg, MessageCallBack callback) {
		this.msg = msg;
		this.callback = callback;
	}

	public void run() {
		try {
			handleMessage(this.msg, this.callback);
		} catch (Exception e) {
			handleExeception(this.msg, e);
		}
	}

	private void handleExeception(Message msg, Exception e) {
		String error = msg.toString() + msg.getSession();
		this.log.error("Handle message error! MSG: " + error, e.getCause());
		Integer errorCount = (Integer) msg.getAttribute("attrib_error_count");
		if (errorCount == null) {
			errorCount = Integer.valueOf(1);
		} else {
			errorCount = Integer.valueOf(errorCount.intValue() + 1);
		}
		msg.setAttribute("attrib_error_count", errorCount);
		short msgType = msg.getType();
		msg.setType((short) (msgType > 0 ? msgType * -1 : msgType));
		msg.setState(201);
		msg.sendBySession();
	}

	private void handleMessage(Message req, MessageCallBack callback)
			throws Exception {
		short reqType = req.getType();
		short resType = (short) (reqType > 0 ? reqType * -1 : reqType);
		Object obj = null;
		// 是角色发送过来的消息，但是无角色id
		if ((callback.isAvatar())
				&& (req.getAttribute("attrib_avatar_id") == null)) {
			req.clear();
			req.setType(resType);
			req.setState(199);
			obj = req;
		} else {
			long start = System.currentTimeMillis();
			try {
				// 解析参数并注入执行
				obj = callback.handle2(req);
				long stop = System.currentTimeMillis();
				long doTime = stop - start;
				if (doTime > TIME_COST_MAX)
					this.log.warn("MSG type={},执行超时>{}ms,TIME={}ms",
							new Object[] { Short.valueOf(req.getType()),
									Integer.valueOf(50), Long.valueOf(doTime) });
			} catch (AdaptorException aex) {
				this.log.warn("", aex);
				req.clear();
				req.setType(resType);
				req.setState(198);
				obj = req;
			}
		}
		if (obj == null) {
			return;
		}
		if ((obj instanceof Message)) {
			Message res = (Message) obj;
			res.setSession(req.getSession());
			res.sendBySession();
		} else if ((obj instanceof Response)) {
			Response response = (Response) obj;
			Message res = new Message(req.getSession());
			res.setType(resType);
			res.setBody(response.toMsg());
			res.sendBySession();
		}
	}
}
