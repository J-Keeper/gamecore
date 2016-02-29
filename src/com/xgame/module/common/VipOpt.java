package com.xgame.module.common;

/**
 * vip特权对应的值
 * 
 * @since 2015年3月30日 下午5:48:31
 * @author YongXinYu
 */
public enum VipOpt {
	/** 建造免费加速时间(已做) */
	FFCONSTRUCT(1, 0, "freeffcons"),
	/** 每日购买能量(已做) */
	BUYENEMY(2, 0, "buyenemy"),
	/** 出战部队 (已做) */
	FIGHTARMY(3, 0, "armys"),
	/** 生产和科研等待队列数量(已做) */
	CONSQUE(4, 0, "consque"),
	/** 联盟礼品 (已做) */
	UNIONGIFT(5, 0, "ungift"),
	/** 配件强化成功率加成(已做) */
	EQSBENIF(7, 0, "eqsbenif"),
	/** 作战单位生产加速 (已做) */
	PRODUBENIF(8, 1016, "produbenif"),
	/** 作战单位改装速度加成(已做) */
	CHBENIF(9, 1015, "chbenif"),
	/** 资源基础容量加成 (已做) */
	RESPROADD(10, 1013, "resproadd"),
	/** 仓库基础保护量加成 (已做) */
	TOREPROADD(11, 1014, "toreproadd"),
	/** 统率升级成功率(已做) */
	LEADBENIF(12, 0, "leadbenif"),
	/** 行军速度加成(已做) */
	MARCH(13, 1011, "march"),
	/** 远征重置次数 (已做) */
	EXPEDRESET(14, 0, "expedreset"),
	/** 升到下一级所需VIP积分 */
	EXP(15, 0, "exp"),
	/** 竞技场匹配次数 */
	JJCMATCH(16, 0, "jjcmatch"),
	/** 竞技场匹配次数 */
	LADDERBUYLIMIT(17, 0, "ladderBuyLimit");

	private int type;
	private int buffId;

	// private String detail;// 字段类型 严格要求跟配置文档一致

	private VipOpt(int type, int buffId, String detail) {
		this.type = type;
		this.buffId = buffId;
		// this.detail = detail;
	}

	public Object getValue(int level) {
		if (level < 0) {
			level = 0;
		}
		if (level > 10) {
			level = 10;
		}
		// Map<Integer, Map<String, Object>> vipMap = AvatarConfig.getInstance()
		// .getVipMap();
		// Map<String, Object> map = vipMap.get(level);
		// Object object = map.get(detail);
		return null;

	}

	public int getType() {
		return type;
	}

	public int getBuffId() {
		return buffId;
	}

}
