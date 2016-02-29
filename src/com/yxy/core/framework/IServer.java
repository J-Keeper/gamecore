package com.yxy.core.framework;

/**
 * @Description: 游戏服务器接口
 * @author YongXinYu
 * @date 2015年8月6日 下午4:25:54
 */
public interface IServer {
	/** 初始化方法 */
	public void init() throws Exception;

	/** 启动服务器方法 */
	public void start() throws Exception;

	/** 关闭服务器方法 */
	public void shutdown();
}