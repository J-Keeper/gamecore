package com.xgame.module.avatar.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.GameContext;
import com.yxy.core.cache.CacheMap;

/**
 * 角色系统数据管理者
 * 
 * @since 2015年2月26日 下午5:57:39
 * @author YongXinYu
 */
@IocBean(create = "init")
public class AvatarManager {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	private AvatarDao avatarDao;
	@Inject("refer:gameContext")
	private GameContext gameContext;

	/** 在线角色ID-角色的关联表 */
	private CacheMap<Long, Avatar> avatarMap;
	/** 全服角色ID-名字的关联表 */
	private Map<Long, String> AID_NAME_MAP;
	/** 全服角色名字-ID的关联表 */
	private Map<String, Long> NAME_AID_MAP;
	/** 全服角色ID-VIP等级的关联表 */
	private Map<Long, Integer> AID_VIP_MAP;
	/** 全服角色ID-等级的关联表 */
	private Map<Long, Integer> AID_LEVEL_MAP;
	/** 全服角色ID-战斗力 */
	private Map<Long, Long> AID_SCORE_MAP;

	/** 全服最大角色编号 */
	private AtomicLong uuid = new AtomicLong();

	/**
	 * 初始化
	 * 
	 * @since 2015年2月26日 下午5:52:16
	 * @author Yongxinyu
	 */
	public void init() {
		avatarMap = new CacheMap<>(AvatarManager.class.getName(), avatarDao);
		AID_NAME_MAP = new ConcurrentHashMap<>();
		NAME_AID_MAP = new ConcurrentHashMap<>();
		AID_VIP_MAP = new ConcurrentHashMap<>();
		AID_LEVEL_MAP = new ConcurrentHashMap<>();
		AID_SCORE_MAP = new ConcurrentHashMap<>();
		long maxId = avatarDao.getMaxId();
		if (maxId < 1000000) {
			maxId = 1000000 * Integer
					.parseInt(gameContext.getConfig("game.id"));
		}
		uuid.set(maxId);
		List<Object[]> tempList = avatarDao.loadAllRoleInfo();
		for (Object[] array : tempList) {
			long id = (Long) array[0];
			String name = (String) array[1];
			int vipLevel = (int) array[2];
			int level = (int) array[3];
			long score = (long) array[4];
			AID_NAME_MAP.put(id, name);
			NAME_AID_MAP.put(name, id);
			AID_VIP_MAP.put(id, vipLevel);
			AID_LEVEL_MAP.put(id, level);
			AID_SCORE_MAP.put(id, score);
		}
		log.info("全服角色数:" + AID_NAME_MAP.size());
	}

	public void addAvatar(Avatar avatar) {
		avatarMap.putWithCache(avatar.getId(), avatar, true);
		NAME_AID_MAP.put(avatar.getName(), avatar.getId());
		AID_NAME_MAP.put(avatar.getId(), avatar.getName());
		AID_VIP_MAP.put(avatar.getId(), avatar.getVipLevel());
		AID_LEVEL_MAP.put(avatar.getId(), avatar.getLevel());
		AID_SCORE_MAP.put(avatar.getId(), avatar.getScore());
	}

	public void remove(long aid, boolean withDB) {
		avatarMap.removeWithCache(aid, withDB);
	}

	public Avatar loadAvatar(long avatarId) {
		return avatarMap.getWithCache(avatarId, true);
	}

	/**
	 * 获取一个在线角色
	 * 
	 * @param avatarId
	 *            角色id
	 * @return
	 * @since 2015年2月28日 下午5:56:40
	 * @author Yongxinyu
	 */
	public Avatar loadOnlineAvatar(long avatarId) {
		return avatarMap.get(avatarId);
	}

	/**
	 * 根据角色id查询一个角色(是否在线未知)
	 * 
	 * @param aid
	 *            角色id
	 * @return
	 * @since 2015年2月28日 下午5:57:09
	 * @author Yongxinyu
	 */
	public Avatar getAvatarByAid(long aid) {
		if (aid < 1000000) {
			return null;
		}
		Avatar avatar = avatarMap.get(aid);
		if (avatar == null) {
			avatar = avatarMap.queryWithDB(aid);
		}
		return avatar;
	}

	/**
	 * 获取一个在线角色
	 * 
	 * @param aid
	 * @return
	 * @since 2015年4月20日 下午6:06:21
	 * @author Yongxinyu
	 */
	public Avatar getAvatar(long aid) {
		return avatarMap.get(aid);
	}

	/**
	 * 根据角色名获取角色
	 * 
	 * @param name
	 *            角色名
	 * @return
	 * @since 2015年2月28日 下午5:57:39
	 * @author Yongxinyu
	 */
	public Avatar getAvatarByName(String name) {
		if (Strings.isBlank(name)) {
			return null;
		}
		Long aid = NAME_AID_MAP.get(name);
		if (aid == null) {
			return null;
		}
		return getAvatarByAid(aid);
	}

	/**
	 * 产生一个唯一的角色ID
	 * 
	 * @return
	 * @since 2015年3月11日 下午3:14:14
	 * @author Yongxinyu
	 */
	public long uuid() {
		return uuid.incrementAndGet();
	}

	public String getName(long aid) {
		return AID_NAME_MAP.get(aid);
	}

	/**
	 * 获取所有在线玩家
	 * 
	 * @return
	 * @since 2015年2月28日 下午5:58:40
	 * @author Yongxinyu
	 */
	public Collection<Avatar> getAll() {
		return avatarMap.values();
	}

	public void updateAvatar(Avatar avatar) {
		avatarMap.update(avatar.getId(), avatar);
	}

	public Set<Long> getAllId() {
		return AID_NAME_MAP.keySet();
	}

	public void time2save() {
		avatarMap.updateAll();
	}

	public boolean isExistName(String name) {
		return NAME_AID_MAP.containsKey(name);
	}

	public void destroy() {
		avatarMap.updateAll();
	}

	public String getAvatarName(long aid) {
		return AID_NAME_MAP.get(aid);
	}

	public Map<Long, Integer> getAidVipMap() {
		return AID_VIP_MAP;
	}

	public Integer getVipLevel(long aid) {
		return AID_VIP_MAP.get(aid);
	}

	public Map<Long, Integer> getLevelMap() {
		return AID_LEVEL_MAP;
	}

	/**
	 * 玩家改名(预留)
	 * 
	 * @param avatar
	 *            角色对象
	 * @param newName
	 *            新名字
	 * @since 2015年2月26日 下午6:18:58
	 * @author Yongxinyu
	 */
	public void rename(Avatar avatar, String newName, byte gender) {
		String oldName = avatar.getName();
		avatar.setName(newName);
		avatar.setGender(gender);
		AID_NAME_MAP.put(avatar.getId(), newName);
		NAME_AID_MAP.put(newName, avatar.getId());
		NAME_AID_MAP.remove(oldName);
	}

	/**
	 * 根据角色名模糊查询相关玩家(预留)
	 * 
	 * @param name
	 *            角色名
	 * @return 对应的玩家
	 * @since 2015年2月26日 下午5:54:45
	 * @author Yongxinyu
	 */
	public Collection<Avatar> seachAvatar(String name) {
		List<Avatar> list = new ArrayList<>();
		for (Entry<String, Long> nameAid : NAME_AID_MAP.entrySet()) {
			String avatarName = nameAid.getKey();
			if (avatarName.contains(name)) {
				long aid = nameAid.getValue();
				Avatar avatar = loadAvatar(aid);
				list.add(avatar);
			}
		}
		return list;
	}

	/**
	 * 获取满足购买成长基金的人数
	 * 
	 * @param needVip
	 * @return
	 * @since 2015年5月25日 上午10:47:26
	 * @author LSQ
	 */
	public int getGtVipCount(int vip, List<Long> buys) {
		int total = 0;
		for (Entry<Long, Integer> entry : AID_VIP_MAP.entrySet()) {
			Long aid = entry.getKey();
			if (entry.getValue() >= vip && !buys.contains(aid)) {
				total++;
			}
		}
		return total;
	}

	public Map<String, Long> getNameIdMap() {
		return NAME_AID_MAP;
	}

	public Map<Integer, Integer> getLoginLevelMap() {
		Map<Integer, Integer> loginLevelMap = new TreeMap<>();
		List<Object[]> loginLevelObjects = avatarDao.getLoginLevelList();
		for (Object[] objs : loginLevelObjects) {
			int level = (int) objs[0];
			int count = (int) objs[1];
			loginLevelMap.put(level, count);
		}
		return loginLevelMap;
	}

	public void update2DB(Avatar avatar) {
		avatarDao.update(avatar.getId(), avatar);
	}

	/**
	 * 更新角色战斗力
	 * 
	 * @param avatar
	 * @since 2015年7月2日 下午2:43:27
	 * @author PCY
	 */
	public void updateAvatarScore(Avatar avatar) {
		AID_SCORE_MAP.put(avatar.getId(), avatar.getScore());
	}

	/**
	 * 获取角色的战斗力
	 * 
	 * @param aid
	 * @return
	 * @since 2015年7月2日 下午2:42:13
	 * @author PCY
	 */
	public long getAvatarScore(long aid) {
		return AID_SCORE_MAP.get(aid);
	}

	/**
	 * 获取玩家的VIP
	 * 
	 * @param aid
	 * @return
	 * @since 2015年7月10日 下午6:05:59
	 * @author PCY
	 */
	public int getAvatarVip(long aid) {
		return AID_VIP_MAP.get(aid);
	}

}
