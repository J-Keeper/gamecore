package com.xgame.module.avatar.conf;

import java.util.List;

import org.nutz.dao.entity.annotation.Table;

import com.xgame.util.ConfUtil;
import com.yxy.core.util.Config;
import com.yxy.core.util.Pair;

/**
 * 等级升级配置
 * 
 * @since 2015年2月27日 上午10:52:31
 * @author YongXinYu
 */
@Config(getKey = "getLevel")
@Table("avatar_levelup")
public class LevelUp {
	/** 等级 */
	@Config
	private int level;
	/** 经验 */
	@Config
	private int exp;
	/** 奖励 */
	@Config(parser = "toPairList", allowNull = true)
	private List<Pair<Integer, Integer>> award;
	/** 带兵数量 */
	@Config
	private int troops;

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public List<Pair<Integer, Integer>> getAward() {
		return award;
	}

	public int getTroops() {
		return troops;
	}

	public List<Pair<Integer, Integer>> toPairList(String str) {
		return ConfUtil.toPairList(str);
	}
}
