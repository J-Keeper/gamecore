package com.yxy.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:游戏线程工厂(减少线程开销)
 * @author YongXinYu
 * @date 2015年8月7日 下午12:12:12
 */
public class GameThreadFactory implements ThreadFactory {
	static final AtomicInteger poolNumber = new AtomicInteger(1);
	final ThreadGroup group;
	final AtomicInteger threadNumber = new AtomicInteger(1);
	final String namePrefix;

	public GameThreadFactory(String name) {
		SecurityManager s = System.getSecurityManager();
		this.group = (s != null ? s.getThreadGroup() : Thread.currentThread()
				.getThreadGroup());
		if (name == null) {
			this.namePrefix = ("def-pool-" + poolNumber.getAndIncrement() + "-thread-");
		} else {
			this.namePrefix = (name + "-pool-" + poolNumber.getAndIncrement() + "-thread-");
		}
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(this.group, r, this.namePrefix
				+ this.threadNumber.getAndIncrement(), 0L);
		if (t.isDaemon()) {
			t.setDaemon(false);
		}
		if (t.getPriority() != 5) {
			t.setPriority(5);
		}
		return t;
	}
}