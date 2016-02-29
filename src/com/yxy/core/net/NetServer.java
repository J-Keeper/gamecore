package com.yxy.core.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.net.codec.Constants;
import com.yxy.core.net.codec.Message;
import com.yxy.core.net.filter.ConnectionFilter;
import com.yxy.core.net.filter.FreqFilter;
import com.yxy.core.net.filter.StatFilter;
import com.yxy.core.net.filter.TimeoutFilter;

/**
 * @Description:网络服务器
 * @author YongXinYu
 * @date 2015年8月7日 上午9:09:35
 */
public class NetServer {
	private Logger log = LoggerFactory.getLogger(getClass());
	private int port;
	private int maxConnections;
	private SocketAcceptor acceptor;
	private ProtocolCodecFactory codecFactory;

	public NetServer(int port, int maxConnections) {
		this.port = port;
		this.maxConnections = maxConnections;
		this.codecFactory = new ObjectSerializationCodecFactory();
	}

	public NetServer(int port, int maxConnections, ProtocolCodecFactory pcf) {
		this.port = port;
		this.maxConnections = maxConnections;
		this.codecFactory = pcf;
	}

	public void startup(IoHandler handler) throws IOException {
		this.acceptor = new NioSocketAcceptor(Runtime.getRuntime()
				.availableProcessors() + 1);
		this.acceptor.getFilterChain().addFirst("codec",
				new ProtocolCodecFilter(this.codecFactory));
		this.acceptor.getFilterChain()
				.addLast("executor", new ExecutorFilter());
		if (Constants.IS_SHOW_LOG) {
			LoggingFilter logf = new LoggingFilter();
			logf.setSessionIdleLogLevel(LogLevel.NONE);
			this.acceptor.getFilterChain().addLast("log", logf);
		}
		this.acceptor.getFilterChain().addLast("conn",
				new ConnectionFilter(this.maxConnections));
		if (Constants.IS_HEART_BEAT) {
			this.acceptor.getFilterChain().addLast("timeout",
					new TimeoutFilter());
		}
		this.acceptor.getFilterChain().addLast("stat", new StatFilter());
		this.acceptor.getFilterChain().addLast("freqFilter",
				new FreqFilter(Constants.freqMap));
		this.acceptor.setReuseAddress(true);
		this.acceptor.getSessionConfig().setSoLinger(1);
		this.acceptor.getSessionConfig().setReuseAddress(true);
		this.acceptor.setHandler(handler);
		this.acceptor.bind(new InetSocketAddress(this.port));
		this.log.info("NetServer listen on port：{}", Integer.valueOf(this.port));
	}

	public SocketAcceptor getAcceptor() {
		return this.acceptor;
	}

	public void broadcast(Message msg) {
		if (this.acceptor.isActive())
			this.acceptor.broadcast(msg);
	}

	public void shutdown() {
		if (this.acceptor != null) {
			this.acceptor.unbind();
			this.acceptor.dispose();
		}
	}
}