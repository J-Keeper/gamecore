package com.xgame.conf;

import com.yxy.core.util.Config;

/**
 * 游戏中key-value类型的键值对配置
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:52:56
 */
public final class KeyValueConf {
	/** 名字最小长度 */
	@Config
	public static int nameMin = 2;
	/** 名字最大长度 */
	@Config
	public static int nameMax = 6;
	/** 初始能量值 */
	@Config(alias = "avtarInitEnergy")
	public static int avtarInitEnergy;

	@Config
	/**能量刷新时间间隔*/
	public static long refEnergyTime;
}
