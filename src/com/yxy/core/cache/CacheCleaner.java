package com.yxy.core.cache;

import java.util.Iterator;

/**
 * @Description: 缓存清理线程
 * @author YongXinYu
 * @date 2015年8月5日 下午9:40:48
 */
public class CacheCleaner extends Thread {
	private long cleanInterval;
	private boolean sleep = false;

	public CacheCleaner(long cleanInterval) {
		this.cleanInterval = cleanInterval;
		setName(getClass().getName());
		setDaemon(true);
	}

	public void setCleanInterval(long cleanInterval) {
		this.cleanInterval = cleanInterval;
		synchronized (this) {
			if (this.sleep)
				interrupt();
		}
	}

	public void run() {
		while (true) {
			try {
				CacheMapManager cmm = CacheMapManager.getInstance();
				Iterator<CacheMap<?, ?>> iterator = cmm.getCacheMaps().values()
						.iterator();
				if (iterator.hasNext()) {
					CacheMap<?, ?> cmap = iterator.next();
					Cache<?, ?> cache = cmap.getCache();
					cache.clean();
					yield();
					continue;
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			this.sleep = true;
			try {
				sleep(this.cleanInterval);
			} catch (Throwable t) {
			} finally {
				this.sleep = false;
			}
		}
	}

}