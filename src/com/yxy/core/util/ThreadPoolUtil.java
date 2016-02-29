package com.yxy.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolUtil {
	private static Logger log = LoggerFactory.getLogger(ThreadPoolUtil.class);

	public static void safeShutdownPool(ExecutorService service) {
		if (service == null)
			return;
		try {
			service.shutdown();
			int count = 1;
			while ((!service.awaitTermination(5L, TimeUnit.SECONDS))
					&& (count < 11)) {
				if (service.isShutdown())
					break;
				log.info("ShutdownNow pool ({})times", Integer.valueOf(count));
				service.shutdownNow();
				count++;
			}
			if (!service.isShutdown())
				log.warn("Pool did not terminate");
		} catch (Exception e) {
			log.error("Shutdown pool error", e);
			service.shutdownNow();
		}
	}
}