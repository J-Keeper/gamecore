package com.yxy.core.framework;

import com.yxy.core.GameContext;

/**
 * @Description:配表操作接口
 * @author YongXinYu
 * @date 2015年8月6日 下午12:16:33
 */
public interface IConfig {

	/** 配表数据加载方法 */
	public void load(GameContext paramGameContext);

	/** 配表数据重新加载载方法 */
	public void reload(GameContext paramGameContext);
}