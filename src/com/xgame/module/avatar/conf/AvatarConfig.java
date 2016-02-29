package com.xgame.module.avatar.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.nutz.dao.entity.Record;

import com.xgame.conf.AbsConfig;
import com.xgame.conf.KeyValueConf;
import com.yxy.core.util.ConfigBuilder;

/**
 * 角色相关配置
 * 
 * @since 2015年2月27日 上午11:16:06
 * @author YongXinYu
 */
public class AvatarConfig extends AbsConfig {
	/** 单例 */
	private static AvatarConfig instance = new AvatarConfig();
	/** 等级升级配置 **/
	private Map<Integer, LevelUp> levelUpMap;

	private AvatarConfig() {
	}

	@Override
	public void setValue() {
		// 初始化全局变量配置
		List<Record> list = dao.query("avatar_init", null);
		Properties prop = new Properties();
		for (Record record : list) {
			prop.put(record.getString("key"), record.getString("value"));
		}
		try {
			ConfigBuilder.autowired(prop, KeyValueConf.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		levelUpMap = new HashMap<>();
		this.loadValue(levelUpMap, LevelUp.class);
	}

	public static AvatarConfig getInstance() {
		return instance;
	}

	public Map<Integer, LevelUp> getLevelUpMap() {
		return levelUpMap;
	}

}
