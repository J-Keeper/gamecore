package com.yxy.core;

import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.resource.Scans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.framework.ISystem;

public class AutoAssemble {
	private static Logger log = LoggerFactory.getLogger(AutoAssemble.class);

	public static void checkIocBean(GameContext context, String pkg) {
		NutIoc ioc = context.getIoc();
		for (Class<?> clazz : Scans.me().scanPackage(pkg)) {
			IocBean bean = (IocBean) clazz.getAnnotation(IocBean.class);
			if (bean != null) {
				log.debug(clazz.getName());
				Object obj = ioc.get(clazz);
				if (ISystem.class.isInstance(obj)) {
					context.addSystem((ISystem) obj);
					context.getEventManager().registerEvent(obj);
				}
				context.getDispatcher().register(obj);
			}
		}
	}

	public static void registerMBean() {
	}
}