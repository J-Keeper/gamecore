package com.yxy.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.net.codec.Message;

public class HashThreadPool {
	private Logger log = LoggerFactory.getLogger(HashThreadPool.class);
	private static final int MAX_THREAD_POOL_SIZE = 64;
	private ExecutorService[] threadGroup;

	public HashThreadPool(int corePoolSize) {
		if ((corePoolSize < 0) || (corePoolSize > MAX_THREAD_POOL_SIZE)) {
			throw new IllegalArgumentException("corePoolSize:" + corePoolSize);
		}
		this.threadGroup = new ExecutorService[corePoolSize];
		for (int i = 0; i < corePoolSize; i++) {
			ExecutorService singleThread = Executors
					.newSingleThreadExecutor(new GameThreadFactory("HashThread"));
			this.threadGroup[i] = singleThread;
		}
	}

	public HashThreadPool() {
		this(Runtime.getRuntime().availableProcessors());
	}

	private int getThreadKey(Object obj) {
		int hash = obj.hashCode();
		return Math.abs(hash % threadGroup.length);
	}

	public void shutdown() {
		for (ExecutorService service : threadGroup)
			safeShutdownPool(service);
	}

	private void safeShutdownPool(ExecutorService service) {
		try {
			service.shutdown();
			int count = 1;
			while ((!service.awaitTermination(5L, TimeUnit.SECONDS))
					&& (count < 11) && (!service.isShutdown())) {
				this.log.info("ShutdownNow hashThread ({})times",
						Integer.valueOf(count));
				service.shutdownNow();
				count++;
			}
			if (!service.isShutdown()) {
				this.log.warn("HashThread did not terminate");
			}
		} catch (Exception e) {
			this.log.error("Shutdown hashThread error", e);
			service.shutdownNow();
		}
	}

	public ExecutorService getThreadGroup(Message msg) {
		int key = 0;
		long sessionId = msg.getSessionId();
		if (sessionId == 0L) {
			key = getThreadKey(msg);
		} else {
			key = getThreadKey(Long.valueOf(sessionId));
		}
		return this.threadGroup[key];
	}

	public ExecutorService getThreadGroup(int key) {
		return this.threadGroup[key];
	}

	public int getPoolSize() {
		return this.threadGroup.length;
	}

	public void submit(Runnable runnable) {
		ExecutorService service = this.threadGroup[getThreadKey(runnable)];
		service.execute(runnable);
	}
}
