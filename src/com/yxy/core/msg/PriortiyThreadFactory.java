package com.yxy.core.msg;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PriortiyThreadFactory implements ThreadFactory {
	private final int priortiy;
	private final String name;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final ThreadGroup threadGroup;

	public PriortiyThreadFactory(String name, int priortiy) {
		this.priortiy = priortiy;
		this.name = name;
		this.threadGroup = new ThreadGroup(name);
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(threadGroup, r);
		t.setName(this.name + "-" + this.threadNumber.getAndIncrement());
		t.setPriority(this.priortiy);
		return t;
	}
}
