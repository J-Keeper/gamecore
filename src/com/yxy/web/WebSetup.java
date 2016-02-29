package com.yxy.web;

import org.nutz.ioc.Ioc;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.GameContext;

public class WebSetup implements Setup {
	Logger log = LoggerFactory.getLogger(getClass());

	public void init(NutConfig config) {
		Ioc ioc = config.getIoc();
		GameContext context = (GameContext) ioc.get(GameContext.class);
		try {
			context.start();
			Logs.get().info("Game Server is up!");
		} catch (Exception e) {
			context.shutdown();
			log.error("Game Server start fail!", e);
		}
	}

	public void destroy(NutConfig config) {
		Ioc ioc = config.getIoc();
		GameContext context = (GameContext) ioc.get(GameContext.class);
		if (context != null)
			context.shutdown();
	}
}