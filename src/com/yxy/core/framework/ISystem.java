package com.yxy.core.framework;

/**
 * @Description: 游戏逻辑子系统接口
 * @author YongXinYu
 * @date 2015年8月6日 下午4:27:51
 */
public interface ISystem {
	/** 配置数据加载方法 */
	public void load() throws Exception;

	/** 配置数据重载方法 */
	public void reload();

	/** 系统初始化方法 */
	public void init();

	/** 定时保存的方法 */
	public void time2save();

	/** 系统关闭方法 */
	public void destroy();
}
