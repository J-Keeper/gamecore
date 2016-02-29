package com.yxy.web;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Lang;

public class WebConfig {
	public static final String WEB_PORT = "game.web.port";
	public static final String WEB_ROOT = "game.web.root";
	public static final String WEB_MODULE = "game.web.module";
	public static final String WEB_CONTEXT = "game.web.context";
	private PropertiesProxy prop;

	public int getWebPort() {
		return prop.getInt("game.web.port");
	}

	public String getWebRoot() {
		return prop.get("game.web.root", "./WebRoot");
	}

	public String getMainModule() {
		return prop.get("game.web.module", "com.wabao.web.MainModule");
	}

	public String getWebContext() {
		return prop.get("game.web.context", "/");
	}

	public WebConfig(String path) {
		this.prop = new PropertiesProxy(new String[] { path });
	}

	public WebConfig(PropertiesProxy prop) {
		this.prop = prop;
	}

	public String check(String key) {
		String val = this.prop.get(key);
		if (null == val)
			throw Lang.makeThrow("Ioc.$conf expect property '%s'",
					new Object[] { key });
		return val;
	}
}