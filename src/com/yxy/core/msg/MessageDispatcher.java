package com.yxy.core.msg;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.framework.IDispatcher;
import com.yxy.core.msg.adp.AdaptorException;
import com.yxy.core.net.codec.Constants;
import com.yxy.core.net.codec.Message;
import com.yxy.core.thread.GameThreadFactory;
import com.yxy.core.thread.HashThreadPool;

public class MessageDispatcher implements IDispatcher {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final int CAPACITY = 1000;
	private static final int LOW_WATER = 10;
	private final Map<Short, MessageCallBack> invokQueue = new HashMap<>();

	private final BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<>(
			CAPACITY);

	private HashThreadPool hashPool;
	private ExecutorService singleThread;
	private volatile boolean isWork;

	public MessageDispatcher(int threadSize) {
		isWork = true;
		hashPool = new HashThreadPool(threadSize);
		singleThread = Executors.newSingleThreadExecutor(new GameThreadFactory(
				"msgDispather"));
	}

	public void start() {
		singleThread.execute(new Runnable() {
			public void run() {
				while (isWork)
					try {
						Message msg = msgQueue.take();
						dispatcher(msg);
					} catch (Exception e) {
						if (isWork) {
							log.error("Dispatcher Error!", e);
						}
					}
			}
		});
	}

	public boolean add(Message msg) {
		if (msg == null) {
			return false;
		}
		if (!isWork) {
			return false;
		}
		if (msgQueue.size() > LOW_WATER) {
			log.warn("msgQueue size: {}", Integer.valueOf(msgQueue.size()));
		}
		if (msg.getType() < 100) {
			log.info("Message type out of range: {}", msg);
			return false;
		}
		if (msgQueue.offer(msg)) {
			return true;
		}
		log.info("The Message discarded to add: {}", msg);
		return false;
	}

	public void stop() {
		while (msgQueue.size() > 0) {
			log.info("stopping dispatcher...");
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
			}
		}
		isWork = false;
		hashPool.shutdown();
		safeShutdownPool(singleThread);
	}

	private void safeShutdownPool(ExecutorService service) {
		try {
			service.shutdown();
			int count = 1;
			while ((!service.awaitTermination(5L, TimeUnit.SECONDS))
					&& (count < 11) && (!service.isShutdown())) {
				log.info("ShutdownNow msgDispather ({})times",
						Integer.valueOf(count));
				service.shutdownNow();
				count++;
			}
			if (!service.isShutdown())
				log.warn("MsgDispather did not terminate");
		} catch (Exception e) {
			log.error("Shutdown msgDispather error", e);
			service.shutdownNow();
		}
	}

	public boolean addInner(Message in) {
		if (msgQueue.offer(in)) {
			return true;
		}
		log.info("The inner Message discarded to add: {}", in.toString());
		return false;
	}

	public void dispatcher(Message msg) {
		ExecutorService exec = hashPool.getThreadGroup(msg);
		MessageCallBack callback = invokQueue.get(Short.valueOf(msg.getType()));
		if (callback != null) {
			exec.submit(new MessageFacadeInvoker(msg, callback));
		} else
			log.info("No method message! {} {}", msg.toString(), msg
					.getSession().toString());
	}

	/**
	 * 注册协议
	 */
	public synchronized void register(Object handler) {
		Class<?> clazz = handler.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			m.setAccessible(true);
			if (m.isAnnotationPresent(Packet.class)) {
				Packet r = (Packet) m.getAnnotation(Packet.class);
				Constants.setFreqMap(Short.valueOf(r.type()),
						Integer.valueOf(r.freq()));
				if (!invokQueue.containsKey(Short.valueOf(r.type()))) {
					try {
						invokQueue.put(Short.valueOf(r.type()),
								new MessageCallBack(handler, m, r.isAvatar()));
					} catch (AdaptorException e) {
						log.warn(
								"注册协议出错:协议编号type={},Class={},MethodName={},isAvatar={}",
								new Object[] { Short.valueOf(r.type()),
										clazz.getSimpleName(), m.getName(),
										Boolean.valueOf(r.isAvatar()) });
					}

					log.debug(
							"注册协议成功:协议编号type={},Class={},MethodName={},isAvatar={}",
							new Object[] { Short.valueOf(r.type()),
									clazz.getSimpleName(), m.getName(),
									Boolean.valueOf(r.isAvatar()) });
				} else {
					log.warn(
							"注册协议号重复:协议编号type={},Class={},MethodName={},isAvatar={}",
							new Object[] { Short.valueOf(r.type()),
									clazz.getSimpleName(), m.getName(),
									Boolean.valueOf(r.isAvatar()) });
				}
			}
		}
	}
}