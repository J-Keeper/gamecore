package com.yxy.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.framework.ISystem;

public class SystemHolder {
	private Logger log = LoggerFactory.getLogger(SystemHolder.class.getClass());
	private Map<String, ISystem> systemContext = new ConcurrentHashMap<>();

	public void add(ISystem sys) {
		if (!systemContext.containsKey(sys.getClass().getSimpleName())) {
			systemContext.put(sys.getClass().getSimpleName(), sys);
			log.info("成功创建 : {}", sys.getClass().getName());
		} else {
			log.warn("重复创建 : {}", sys.getClass().getName());
		}
	}

	public ISystem getSystem(String name) {
		return systemContext.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSystem(Class<T> clazz) {
		String className = clazz.getSimpleName();
		return (T) systemContext.get(className);
	}

	public Map<String, ISystem> getSystemContext() {
		return systemContext;
	}

	public void initAllSystem() throws Exception {
		for (ISystem sys : systemContext.values()) {
			sys.init();
		}
		this.log.info("All system init() end");
	}

	public void time2save() {
		for (ISystem sys : this.systemContext.values()) {
			try {
				sys.time2save();
			} catch (Exception e) {
				log.error("", e);
			}
		}
		log.info("All system time2save() end");
	}

	public void destroy() {
		for (ISystem sys : systemContext.values()) {
			try {
				long start = System.currentTimeMillis();
				sys.destroy();
				long cost = System.currentTimeMillis() - start;
				if (cost > 30000L) {
					log.info(sys.getClass().getName() + " destroy cost:" + cost
							+ "ms");
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
		systemContext.clear();
		log.info("All system destroy() end");
	}
}