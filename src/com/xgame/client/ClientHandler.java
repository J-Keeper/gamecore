package com.xgame.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.random.R;

import com.xgame.packet.MessageState;
import com.xgame.packet.MessageType;
import com.yxy.core.net.codec.Message;

/**
 * @author YXY
 * @date 2014年7月7日 下午3:29:56
 */
public class ClientHandler extends IoHandlerAdapter {

	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("发送>> " + message);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		System.err.println("#异常# " + cause.getMessage());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Game Over");
		System.exit(0);
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		Message msg = (Message) message;
		if (msg.getState() == MessageState.SUC) {
			System.out.println("接收<< " + msg);
		} else {
			System.err.println("接收<< " + msg);
		}
		try {
			switch (msg.getType()) {
			case MessageType.LOGIN_RES:
				int avatarId = msg.getBody().getInteger("avatarId");
				if (avatarId == 0) {
					createAvatar(session, msg);
				} else {
					loadAvatar(session, msg);
				}
				break;
			case MessageType.AVATAR_CREATE_RES:
			case MessageType.AVATAR_LOAD_RES:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建角色
	 * 
	 * @param session
	 * @param msg
	 */
	private void createAvatar(IoSession session, Message msg) {
		msg.clear();
		msg.setType(MessageType.AVATAR_CREATE_REQ);
		msg.put("name", "新项" + R.random(1, 100));
		msg.put("gender", R.random(1, 2));
		session.write(msg);
	}

	/**
	 * 加载角色
	 * 
	 * @param session
	 * @param msg
	 */
	private void loadAvatar(IoSession session, Message msg) {
		msg.clear();
		msg.setType(MessageType.AVATAR_LOAD_REQ);
		session.write(msg);
	}

}
