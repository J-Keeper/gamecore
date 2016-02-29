package com.yxy.core.net;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.net.codec.Message;

/**
 * @Description:模拟客户端
 * @author YongXinYu
 * @date 2015年8月6日 下午10:22:43
 */
public class Client {
	private Logger log = LoggerFactory.getLogger(getClass());
	/** 连接超时时间 10s */
	private static final long CONNECT_TIMEOUT = 10000L;
	private String hostname = "localhost";
	private int port = 8080;
	private ProtocolCodecFactory codecFactory;
	private boolean isWork = true;

	private NioSocketConnector connector = null;
	private IoSession session;

	public Client(String hostname, int port, ProtocolCodecFactory pcf) {
		this.hostname = hostname;
		this.port = port;
		this.codecFactory = pcf;
	}

	/**
	 * @Description: 客户端启动
	 * @param @param handler
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean startup(IoHandler handler) {
		if ((this.session != null) && (this.session.isConnected())) {
			this.log.warn("Already connected. Disconnect first.");
			return false;
		}
		while (isWork) {
			this.connector = new NioSocketConnector();
			this.connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
			// 添加编解码工厂
			this.connector.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(this.codecFactory));
			// 设置业务逻辑处理器
			this.connector.setHandler(handler);
			try {
				// 家里连接
				ConnectFuture future = this.connector
						.connect(new InetSocketAddress(this.hostname, this.port));
				future.awaitUninterruptibly();
				if (!future.isConnected()) {
					this.log.warn("Can't connect to " + this.hostname + ":"
							+ this.port + ", try again!");
					future.cancel();
					this.connector.dispose();
					Thread.sleep(2000L);
				} else {
					this.session = future.getSession();
					this.log.info("success connect to " + this.hostname + ":"
							+ this.port);
					return true;
				}
			} catch (Exception e) {
				this.log.error("Failed to connect.", e);
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException localInterruptedException) {
				}
			}
		}
		return isWork;
	}

	/**
	 * @Description: 发送消息
	 * @param @param msg
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean sendMessage(Message msg) {
		boolean ans = false;
		if ((this.session != null) && (this.session.isConnected())) {
			msg.setSession(this.session);
			this.session.write(msg);
			ans = true;
		}
		return ans;
	}

	public boolean isConn() {
		return this.connector.isActive();
	}

	public IoSession getSession() {
		return this.session;
	}

	/**
	 * @Description:关闭客户端
	 * @param
	 * @return void
	 * @throws
	 */
	public void shutdown() {
		this.isWork = false;
		if (this.session != null) {
			this.session.close(true);
		}
		if (this.connector != null)
			this.connector.dispose();
	}
}