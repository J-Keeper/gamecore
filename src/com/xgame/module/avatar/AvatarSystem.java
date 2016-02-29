package com.xgame.module.avatar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xgame.event.EventType;
import com.xgame.module.account.AccountSystem;
import com.xgame.module.account.dao.Account;
import com.xgame.module.avatar.conf.AvatarConfig;
import com.xgame.module.avatar.dao.Avatar;
import com.xgame.module.avatar.dao.AvatarDao;
import com.xgame.module.avatar.dao.AvatarManager;
import com.xgame.module.common.Money;
import com.xgame.packet.MessageState;
import com.xgame.packet.MessageType;
import com.yxy.core.GameContext;
import com.yxy.core.event.Event;
import com.yxy.core.event.Listener;
import com.yxy.core.framework.SystemAdapter;
import com.yxy.core.msg.MessageConstant;
import com.yxy.core.msg.Optional;
import com.yxy.core.msg.Packet;
import com.yxy.core.net.codec.Message;
import com.yxy.core.sch.Job;
import com.yxy.core.sch.Trigger;
import com.yxy.core.sch.impl.DailyTrigger;

/**
 * 游戏角色系统
 * 
 * @since 2015年2月26日 下午3:55:42
 * @author YongXinYu
 */
@IocBean(create = "load")
public class AvatarSystem extends SystemAdapter {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	private AvatarManager avatarManager;
	@Inject("refer:gameContext")
	private GameContext context;
	@Inject
	private AccountSystem accountSystem;
	@Inject
	private AvatarDao avatarDao;

	private AvatarConfig avatarConfig = AvatarConfig.getInstance();

	@Override
	public void reload() {
		avatarConfig.reload(context);
	}

	@Override
	public void load() throws Exception {
		// 加载配置
		avatarConfig.load(context);
	}

	@Override
	public void init() {
		// 每日定时任务,以更新所有在线玩家数据为例
		context.schedule(new DailyTrigger("Confer", String.valueOf("00:00:00"),
				new Job() {
					@Override
					public void execute(Trigger trigger) {
						// 更新所有角色数据
						Stopwatch sw = Stopwatch.begin();
						avatarManager.time2save();
						sw.stop();
						log.info("数据更新完成,耗时: " + sw.getDuration() + "ms");
					}
				}));
	}

	@Override
	public void time2save() {
		avatarManager.time2save();
	}

	@Override
	public void destroy() {
		avatarManager.destroy();
	}

	/**
	 * 上线处理
	 * 
	 * @param event
	 * @since 2015年2月26日 下午6:00:48
	 * @author Yongxinyu
	 */
	@Listener(EventType.ONLINE)
	private void onLine(Event event) {
		Avatar avatar = (Avatar) event.getSource();
		avatar.setLoginTime(new Date());
		refreshEnergy(avatar, new Date());
	}

	/**
	 * 上线后处理
	 * 
	 * @param event
	 * @since 2015年2月26日 下午6:01:10
	 * @author Yongxinyu
	 */
	@Listener(EventType.ONLINEAFTER)
	private void onlineAfter(Event event) {
		Avatar avatar = (Avatar) event.getSource();
		log.info(avatar.getId() + "上线后处理");
	}

	/**
	 * 角色升级处理
	 * 
	 * @param event
	 * @since 2015年2月26日 下午6:01:38
	 * @author Yongxinyu
	 */
	@Listener(EventType.AVA_LEVELUP)
	private void levelUp(Event event) {
		Avatar avatar = (Avatar) event.getSource();
		log.info(avatar.getId() + "升级事件处理");
	}

	/**
	 * 下线处理
	 * 
	 * @param event
	 * @since 2015年2月26日 下午6:01:52
	 * @author Yongxinyu
	 */
	@Listener(EventType.OFFLINE)
	private void offline(Event event) {
		Avatar avatar = (Avatar) event.getSource();
		log.info(avatar.getName() + "下线事件处理");
	}

	/**
	 * 请求创建角色
	 * 
	 * @param req
	 * @return
	 * @since 2015年2月26日 下午7:01:09
	 * @author Yongxinyu
	 */
	@Packet(type = MessageType.AVATAR_CREATE_REQ, isAvatar = false)
	public Message createAvatar(Message req) {
		String username = (String) req
				.getAttribute(MessageConstant.ATTR_USERNAME);
		byte gender = req.getBody().getByteValue("gender");

		req.clear();
		req.setType(MessageType.AVATAR_CREATE_RES);
		// 角色存在性校验
		final Account account = accountSystem.getAccount(username);
		if (account.getAvatarId() != 0) {
			req.put("info", "目前限定一个账户只能有一个角色，你已经有角色了!");
			req.setState(MessageState.DUP_CREATE);
			return req;
		}
		// 创建角色
		String icon = "man.jpg";
		if (gender == 2) {
			icon = "woman.jpg";
		}
		long avatarId = avatarManager.uuid();
		String name = String.valueOf(avatarId);
		Avatar avatar = new Avatar(username, avatarId, name, gender, icon);
		avatarManager.addAvatar(avatar);

		// 异步通知帐号服
		context.submit(new Runnable() {
			@Override
			public void run() {
				try {
					// String notifyURL = context.getConfig("url.createNotify");
					// Map<String, Object> params = new HashMap<>();
					// params.put("serverId", account.getSid());
					// params.put("account", account.getName());
					// params.put("channel", account.getPlatform());
					// Http.post2(notifyURL, params, 5000);
				} catch (Exception e) {
					log.error("", e);
				}
			}
		});

		// 创建成功-发布相关事件
		avatar.setOnline(true);
		Event create = new Event(EventType.CREATE_AVATAR, avatar);
		context.addEvent(create);

		req.put("avatar", avatar.toMsg());
		Event online = new Event(EventType.ONLINE, avatar);
		context.addEvent(online);
		return req;
	}

	/**
	 * 请求加载角色
	 * 
	 * @param reconn
	 *            0是正常上线 1是重连
	 * @param req
	 * @return
	 * @since 2015年2月26日 下午8:52:02
	 * @author Yongxinyu
	 */
	@Packet(type = MessageType.AVATAR_LOAD_REQ, isAvatar = false)
	public Message loadAvatar(@Optional("reconn") int reconn, Message req) {
		String username = (String) req
				.getAttribute(MessageConstant.ATTR_USERNAME);
		req.clear();
		req.setType(MessageType.AVATAR_LOAD_RES);
		// 角色存在性校验
		Account account = accountSystem.getAccount(username);
		long avatarId = account.getAvatarId();
		if (avatarId == 0) {
			req.setState(MessageState.NEED_AVATAR);
			req.put("info", "请创建角色");
			return req;
		}
		// 可能存在服务器内部错误
		Avatar avatar = avatarManager.loadAvatar(avatarId);
		if (avatar == null) {
			req.setState(MessageState.NEED_AVATAR);
			req.put("info", "请创建角色");
			log.warn("加载角色失败,需要重新创建,aid={}", avatarId);
			return req;
		}
		avatar.setOnline(true);
		// 加载成功-发布相关事件
		Event online = new Event(EventType.ONLINE, avatar);
		context.addEvent(online);
		req.put("avatar", avatar.toMsg());
		return req;
	}

	/**
	 * 请求能量恢复
	 * 
	 * @param msg
	 * @return
	 * @since 2015年2月27日 上午9:17:05
	 * @author Yongxinyu
	 */
	@Packet(type = MessageType.AVATAR_ENERGYRECV_REQ)
	public Message recoverEnergy(Message msg) {
		Avatar avatar = getAvatar(msg);
		msg.clear();
		msg.setType(MessageType.AVATAR_ENERGYRECV_RES);
		Date nowDate = new Date();
		msg.put("power", refreshEnergy(avatar, nowDate));
		msg.put("eUT", avatar.getFullEnergyTimes());
		return msg;
	}

	/**
	 * @Description: 从session中获取对应的角色(只针对在线角色)
	 * @author Yongxinyu
	 * @param msg
	 * @return Avatar
	 */
	public Avatar getAvatar(Message msg) {
		long avatarId = (long) msg.getAttribute(MessageConstant.ATTR_AVATAR_ID);
		return getAvatar(avatarId);
	}

	/**
	 * 加资源-只支持在线玩家
	 * 
	 * @param avatar
	 *            要加的角色对象
	 * @param money
	 *            要加的资源对象
	 * @return
	 * @since 2015年2月28日 下午6:04:45
	 * @author Yongxinyu
	 */
	public int addMoney(Avatar avatar, Money money) {
		int res = MessageState.SUC;
		if (money.getValue() == 0) {
			return res;
		}
		if (money.getValue() < 0) {
			log.info("加资源参数错误:" + money);
			return MessageState.PARAMS_ERR;
		}
		if (avatar == null) {
			log.info("加资源的对象不存在");
			return MessageState.PARAMS_ERR;
		}
		short type = money.getType();
		long aid = avatar.getId();
		Message msg = new Message(MessageType.AVATAR_UPDATE_PUSH);
		synchronized (avatar) {
			switch (type) {
			case Money.energy:
				int energy = avatar.getEnergy();
				avatar.setEnergy(energy + money.getValue());
				msg.put("energy", avatar.getEnergy());
				msg.put("eUT", avatar.getFullEnergyTimes());
				break;
			case Money.diamond:
				int diamond = avatar.getDiamond();
				avatar.setDiamond(diamond + money.getValue());
				msg.put("diamond", avatar.getDiamond());
				break;
			case Money.prestige:
				break;
			case Money.honor:
				break;
			case Money.exp:
				msg.put("exp", avatar.getExp());
				msg.put("level", avatar.getLevel());
			default:
				res = MessageState.PARAMS_ERR;
				break;
			}
			accountSystem.sendMsg(aid, msg);
		}
		if (!avatar.isOnline()) {
			avatarDao.update(avatar.getId(), avatar);
		}
		return res;
	}

	/**
	 * 扣除资源
	 * 
	 * @param avatar
	 * @param money
	 * @return 扣除是否成功
	 * @since 2015年2月28日 下午6:09:10
	 * @author Yongxinyu
	 */
	public int decMoney(Avatar avatar, Money money) {
		int flag = MessageState.SUC;
		if (money.getValue() == 0) {
			return flag;
		}
		if (money.getValue() < 0) {
			log.info("扣资源参数错误" + money);
			return MessageState.PARAMS_ERR;
		}
		if (avatar == null) {
			log.info("扣资源的角色不存在");
			return MessageState.PARAMS_ERR;
		}
		short type = money.getType();
		long aid = avatar.getId();
		Message msg = new Message(MessageType.AVATAR_UPDATE_PUSH);
		synchronized (avatar) {
			switch (type) {
			case Money.energy:
				int energy = avatar.getEnergy();
				if (energy < money.getValue()) {
					return MessageState.ENERGY_LACK;
				}
				avatar.setEnergy(energy - money.getValue());
				msg.put("energy", avatar.getEnergy());
				msg.put("energyUT", avatar.getFullEnergyTimes());
				break;
			case Money.diamond:
				int diamond = avatar.getDiamond();
				if (diamond < money.getValue()) {
					return MessageState.DIAMOND_LACK;
				}
				avatar.setDiamond(diamond - money.getValue());
				msg.put("diamond", avatar.getDiamond());
				break;
			default:
				flag = MessageState.PARAMS_ERR;
				break;
			}
			accountSystem.sendMsg(aid, msg);
		}
		return flag;
	}

	/**
	 * 检查资源
	 * 
	 * @param avatar
	 * @param resource
	 *            待校验资源值
	 * @return 充足时返回MessageState.SUC,否则返回各自的状态
	 * @since 2015年2月28日 下午6:08:23
	 * @author Yongxinyu
	 */
	public int checkMoney(Avatar avatar, Money money) {
		int flag = MessageState.SUC;
		if (money.getValue() < 1) {
			throw new IllegalArgumentException("检测资源参数错误" + money);
		}
		if (avatar == null) {
			throw new IllegalArgumentException("检查资源对象角色不存在");
		}
		short type = money.getType();
		switch (type) {
		case Money.energy:
			int energy = avatar.getEnergy();
			if (energy < money.getValue())
				flag = MessageState.ENERGY_LACK;
			break;
		case Money.diamond:
			int diamond = avatar.getDiamond();
			if (diamond < money.getValue())
				flag = MessageState.DIAMOND_LACK;
			break;
		case Money.honor:
			break;
		case Money.prestige:
			break;
		default:
			break;
		}
		return flag;
	}

	/**
	 * 根据角色ID获取一个角色信息(是否在线未知)
	 * 
	 * @param avatarId
	 * @return
	 * @since 2015年2月28日 下午5:57:09
	 * @author Yongxinyu
	 */
	public Avatar getAvatar(long avatarId) {
		return avatarManager.getAvatarByAid(avatarId);
	}

	/**
	 * 根据角色名获取一个角色
	 * 
	 * @param name
	 * @return
	 * @since 2015年2月28日 下午5:57:09
	 * @author Yongxinyu
	 */
	public Avatar getAvatarByName(String name) {
		return avatarManager.getAvatarByName(name);
	}

	/**
	 * 获取所有在线玩家
	 * 
	 * @return
	 * @since 2015年2月28日 下午5:57:09
	 * @author Yongxinyu
	 */
	public List<Avatar> getAllAvatar() {
		return new ArrayList<Avatar>(avatarManager.getAll());
	}

	/**
	 * 获取全服玩家的id
	 * 
	 * @return
	 * @since 2015年2月28日 下午6:11:39
	 * @author Yongxinyu
	 */
	public Set<Long> getAllId() {
		return avatarManager.getAllId();
	}

	/**
	 * 根据id查询某个玩家是否在线
	 * 
	 * @param aid
	 * @return
	 * @since 2015年3月23日 上午10:04:56
	 * @author Yongxinyu
	 */
	public boolean isOnLine(long aid) {
		Avatar avatar = avatarManager.getAvatar(aid);
		if (avatar == null) {
			return false;
		}
		return avatar.isOnline();
	}

	/**
	 * 刷新角色能量
	 * 
	 * @param avatar
	 * @return 刷新后角色的能量值
	 * @since 2015年3月10日 下午6:04:08
	 * @author Yongxinyu
	 */
	private int refreshEnergy(Avatar avatar, Date nowDate) {
		return avatar.getEnergy();
	}

	/**
	 * 获取玩家的角色名字(无论在线与否)
	 * 
	 * @param aid
	 * @return
	 * @since 2015年4月22日 下午8:05:40
	 * @author Yongxinyu
	 */
	public String getAvatarName(long aid) {
		return avatarManager.getName(aid);
	}

	/**
	 * 获取玩家的vip等级(无论在线与否)
	 * 
	 * @param aid
	 * @return
	 * @since 2015年4月22日 下午8:06:10
	 * @author Yongxinyu
	 */
	public int getVipLevel(long aid) {
		return avatarManager.getVipLevel(aid);
	}

	/**
	 * 获取玩家
	 * 
	 * @param aid
	 * @return
	 * @since 2015年4月23日 下午8:59:36
	 * @author Yongxinyu
	 */
	public int getLevel(long aid) {
		return avatarManager.getLevelMap().get(aid);
	}

	/**
	 * 获取玩家战斗力
	 * 
	 * @param aid
	 * @return
	 * @since 2015年7月2日 下午2:44:29
	 * @author PCY
	 */
	public long getScore(long aid) {
		return avatarManager.getAvatarScore(aid);
	}

	public int getGtVipCount(int vip, List<Long> buys) {
		return avatarManager.getGtVipCount(vip, buys);
	}

	public List<Avatar> seachAvatar(String name, long startDate, long endDate) {
		// 查询方式判定
		int type = startDate == 0 ? (endDate == 0 ? 0 : 1) : (endDate == 0 ? 2
				: 3);
		List<Avatar> list = new ArrayList<>();
		for (Entry<String, Long> nameAid : avatarManager.getNameIdMap()
				.entrySet()) {
			String avatarName = nameAid.getKey();
			if (name == null || name.trim().equals("")
					|| avatarName.indexOf(name.trim()) != -1) {
				long aid = nameAid.getValue();
				Avatar avatar = getAvatar(aid);
				Date registerDate = avatar.getCreateDate();
				if (registerDate == null) {
					continue;
				}
				long registerTime = registerDate.getTime();
				switch (type) {
				case 0:// 无限定时间
					list.add(avatar);
					break;
				case 1:// 限定结束时间
					if (registerTime <= endDate) {
						list.add(avatar);
					}
					break;
				case 2:// 限定开始时间
					if (startDate <= registerTime) {
						list.add(avatar);
					}
					break;
				case 3:// 同时限定开始时间和结束时间
					if ((startDate <= registerTime)
							&& (registerTime <= endDate)) {
						list.add(avatar);
					}
					break;
				default:
					break;
				}
			}
		}
		return list;
	}

	public Map<Integer, Integer> getlevelCount() {
		Map<Integer, Integer> levelCountMap = new TreeMap<Integer, Integer>();

		for (int level : avatarManager.getLevelMap().values()) {
			Integer count = levelCountMap.get(level);
			if (count == null)
				count = 1;
			else
				count++;
			levelCountMap.put(level, count);
		}
		return levelCountMap;
	}

	/**
	 * 获取玩家VIP
	 * 
	 * @param aid
	 * @return
	 * @since 2015年7月10日 下午6:07:18
	 * @author PCY
	 */
	public int getAvatarVip(long aid) {
		return avatarManager.getAvatarVip(aid);
	}

}
