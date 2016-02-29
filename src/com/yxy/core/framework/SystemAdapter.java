package com.yxy.core.framework;

/**
 * @Description: 游戏逻辑系统适配器
 * @author YongXinYu
 * @date 2015年8月6日 下午4:30:06
 */
public class SystemAdapter implements ISystem {
	/**
	 * 服务器启动，系统加载时使用
	 */
	public void load() throws Exception {
	}

	/**
	 * 服务器重新启动，系统加载时使用
	 */
	public void reload() {
	}

	/**
	 * 服务器5min定时调用
	 */
	public void time2save() {
	}

	/**
	 * 服务器销毁时使用
	 */
	public void destroy() {
	}

	/**
	 * 服务器初始化时使用
	 */
	public void init() {
	}
}