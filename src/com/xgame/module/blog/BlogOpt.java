package com.xgame.module.blog;

/**
 * 操作选项,注意:字段命名尽量限制在30字符以内!
 * 
 * @author yaowenhao
 * @date 2014年10月9日 下午1:54:07
 */
public enum BlogOpt {
	NA,
	/** 挑战副本 */
	dragon_challenge,
	/** 领取副本宝箱 */
	dragon_getbox,
	/** 挑战补给线 */
	feedline_challenge,
	/** 扫荡补给线 */
	feedline_sweep,
	/** 重置补给线 */
	feedline_reset,
	/** 英雄副本挑战 */
	hdragon_challenge,
	/** 英雄副本购买挑战次数 */
	hdragon_buychallenge,
	/** 建筑加速 */
	build_speed,
	/** 建筑升级 */
	build_level_up,
	/** 建筑升级取消 */
	build_level_cancel,
	/** 建筑建造 */
	build_create,
	/** 生产加速 */
	produce_speed,
	/** 生产战机 */
	produce_plane,
	/** 生产道具 */
	produce_item,
	/** 生产战机完成 */
	produce_plane_done,
	/** 获取任务奖励 */
	getReward_task,
	/** 移除限时任务 */
	delete_timerTask,
	/** 每日签到 */
	everyday_sign,
	/** 添加好友 */
	add_friend,
	/** 删除好友 */
	delete_friend,
	/** 屏蔽玩家 */
	shield_avatar,
	/** 解除屏蔽玩家 */
	reliShield_avatar,
	/** 使用物品 */
	used_item,
	/** 普通商店购买物品 */
	commonShop_buyItem,
	/** 分解配件 */
	decom_parts,
	/** 获得配件碎片 */
	get_debris,
	/** 分解配件碎片 */
	decom_debris,
	/** 强化配件 */
	streng_parts,
	/** 改造配件 */
	reform_parts,
	/** 合成配件 */
	compo_parts,
	/** 兑换配件 */
	cash_parts,
	/** 接收系统邮件 */
	system_mail,
	/** 获取邮件奖励 */
	getMail_reward,
	/** 招募英雄(消耗钻石) */
	hero_buy,
	/** 英雄分解 */
	hero_resolve,
	/** 英雄合成 */
	hero_compose,
	/** 英雄升星 */
	hero_start,
	/** 英雄升级 */
	hero_level,
	/** 获得英雄 */
	hero_get,
	/** 创建联盟 */
	league_create,
	/** 联盟捐献 */
	league_dedicate,
	/** 竞技场挑战 */
	arena_challenge,
	/** 竞技场匹配 */
	arena_match,
	/** 竞技场补充匹配次数 */
	arena_addmatchs,
	/** 竞技场领取称号奖励 */
	arena_get4trward,
	/** 竞技场领取排名奖励 */
	arena_getrrward,
	/** 竞技场颁发排名奖励 */
	arena_setrrward,

	/** 创建角色 */
	avatar_create,
	/** 角色主动请求恢复能量 */
	avatar_recoverenergy,
	/** 角色充值 */
	avatar_recharge,
	/** 角色升级统帅 */
	avatar_leadupper,
	/** 角色购买声望 */
	avatar_buyprestige,
	/** 角色购买能量 */
	avatar_buyenergy,
	/** 角色改名 */
	avatar_rename,
	/** 角色上线 */
	avatar_login,
	/** 角色下线 */
	avatar_logout,
	/** 新手引导 */
	guide,

	/** 科技升级请求 */
	science_upperreq,
	/** 科技升级完成 */
	science_upperover,
	/** 科技升级加速 */
	science_speedup,
	/** 科技升级取消 */
	science_cancel,

	/** 技能升级 */
	skill_upper,
	/** 技能重置 */
	skill_reset,

	/** 世界地图攻击城市 */
	map_attcity,
	/** 世界地图城市防守 */
	map_citydefener,
	/** 世界地图挖矿 */
	map_attminery,
	/** 世界地图协防城市 */
	map_helpcity,
	/** 世界地图任务撤销会召回 */
	map_callback,
	/** 世界地图攻击矿点的玩家 */
	map_attplayer,
	/** 世界地图矿点玩家 防守 */
	map_playerdefener,
	/** 世界地图玩家使用了防护罩 */
	map_useprotect,
	/** 世界地图侦查 */
	map_spy,
	/** 世界地图搬迁 */
	map_movecity,
	/** 世界地图任务加速 */
	map_speedup,

	/*** 购买成长基金 */
	buy_growthFund,
	/*** 领取成长基金 */
	get_growthFund_reward,
	/** 领取你闯关我送礼活动奖励 */
	get_dragonAct_reward,
	/** 你闯关我送礼活动排行榜 */
	get_levelAct_reward,
	/** 战力大作战活动奖励 */
	get_scoreAct_reward,
	/** 战机暴风活动合成飞机 */
	planeStorm_compo_plane,
	/** 战机暴风活动抽奖 */
	planeStorm_lottery,
	/** 领取紫将收集令 */
	get_purColl_reward,
	/** 领取紫将升级令 */
	get_purUp_reward,
	/** 限购活动购买物品 */
	buy_quotaAct_item,
	/** 拉霸抽奖 */
	slots_lottery,
	/** 领取雷霆行动奖励 */
	get_thunderAct_reward,
	/** 添加雷霆行动的次数 */
	add_thunderAct_time,
	/** 等级大礼包活动奖励 */
	get_levelGiftAct_reward,
	/** CDK兑换 */
	cdk_exchange,
	/** 联盟商店 */
	league_shop,
	/** 在线领奖 */
	online_reward,
	/** 联盟等级活动奖励领取 */
	get_unionlv_reward,
	/** 联盟等级活动(个人捐献)奖励领取 */
	get_perDed_reward,
	/** 联盟战力活动奖励领取 */
	get_unionSc_reward,
	/** 联盟战力活动(个人战力)奖励领取 */
	get_perSc_reward,
	/** 联盟捐献活动奖励领取 */
	get_uniondon_reward,
	/** 联盟捐献活动(个人捐献)奖励领取 */
	get_perdon_reward,
	/** 竞技场积分活动-抽奖 */
	act_arenascore_reward,
	/** 竞技场连胜活动-领奖 */
	act_arenals_reward,
	/** 登陆抽奖 */
	login_lottery,
	/** 登陆奖励邮件 */
	online_getReward_mail,
	/** 紫装售卖 */
	purple_part_sell,
	/** 联盟资金募集 */
	union_funds_activity,
	/** 首充大礼包 */
	first_recharge_reward,
	/** 充值送豪礼 */
	recharge_gift_reward,
	/** 充值红包 */
	recharge_packet_reward,
	/** 充值积分抽奖 */
	vip_point_lottery,
	/** 天梯活动 兑换物品 */
	ladder_cashGoods,

	/** 远征挑战 */
	expedite_fight,
	/** 远征领取某个关卡的奖励 */
	expedite_getreward,
	/** 远征积分兑换物品 */
	expedite_exchangegoods,
	/** 远征手动刷新物品 */
	expedite_refreshgoods,
	/** 远征手动重置关卡 */
	expedite_resetoutpost,

	/** 天梯每天数据重置 */
	ladder_day_reset,
	/** 天梯挑战对手 */
	ladder_atk,
	/** 天梯被挑战 */
	ladder_def,
	/** 天梯购买挑战次数 */
	ladder_buy_time,
	/** 天梯积分兑换 */
	ladder_exchange,

	/** 联盟训练场攻打副本 */
	train_atcv_dargon,
	/** 联盟训练场领取宝箱 */
	train_get_chests,

	/** 联盟争霸报名 */
	hegemony_apply,
	/** 联盟争霸取消报名 */
	hegemony_undo_apply,

}
