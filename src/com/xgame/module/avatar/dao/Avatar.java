package com.xgame.module.avatar.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xgame.conf.KeyValueConf;
import com.xgame.packet.MessageSerializable;
import com.xgame.util.DateUtil;

/**
 * 游戏角色对象(该对象将被序列化后存入数据库)
 * 
 * @since 2015年2月23日 下午5:47:49
 * @author YongXinYu
 */
public class Avatar implements Serializable, MessageSerializable {
	private static final long serialVersionUID = 1L;
	/** 1.角色id */
	private long id;
	/** 2.关联的账号 **/
	private String account;
	/** 3.名字 **/
	private String name;
	/** 4.性别 (1-男,2-女) */
	private byte gender;
	/** 5.主角形象Id */
	private String icon;

	/** 6.当前经验 **/
	private int exp;
	/** 7.当前等级 **/
	private int level;
	/** 8.历史总经验 **/
	private int expSum;

	/** 9.能量 **/
	private int energy;
	/** 10.最近一次能量更新时间 */
	private Date energyUT;

	/** 11.VIP积分 **/
	private int vipPoint;
	/** 12.VIP等级 **/
	private int vipLevel;
	/** 13.钻石 **/
	private int diamond;

	/** 14.是否在线 */
	private boolean isOnline;
	/** 15.最近一次登陆时间 */
	private Date loginTime;
	/** 16.最近一次退出 */
	private Date logoutTime;
	/** 17. 玩家总在线时间 秒 */
	private long onlineSum;
	/** 18.角色创建时间 */
	private Date createDate;
	/** 19.战力 */
	private long score;
	/** 20.历史最高战力 */
	private long maxBats;

	/** 充值信息 */
	private Recharge charge;

	public Avatar() {
	}

	public Avatar(String account, long aid, String name, byte gender,
			String icon) {
		this.id = aid;
		this.account = account;
		this.name = name;
		this.gender = gender;
		this.icon = icon;
		this.level = 1;
		this.energy = KeyValueConf.avtarInitEnergy;
		this.energyUT = new Date();
		this.loginTime = new Date();
		this.logoutTime = new Date();
		this.createDate = new Date();
		this.diamond = 1000;
	}

	/**
	 * 恢复满全部能量所需时间(毫秒)
	 * 
	 * @return
	 * @since 2015年3月2日 下午2:22:45
	 * @author Yongxinyu
	 */
	public long getFullEnergyTimes() {
		if (KeyValueConf.avtarInitEnergy <= energy) {
			return 0;
		}
		long diffEnergy = KeyValueConf.avtarInitEnergy - energy;
		long diffSeconds = DateUtil.diffSecond(energyUT, new Date());
		long energyRefTime = KeyValueConf.refEnergyTime;
		return energyRefTime * diffEnergy - diffSeconds;
	}

	@Override
	public Map<String, Object> toMsg() {
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("id", id);
		msg.put("name", name);
		msg.put("gender", gender);
		msg.put("icon", icon);

		msg.put("exp", exp);
		msg.put("level", level);

		msg.put("energy", energy);
		msg.put("energyUT", getFullEnergyTimes());

		msg.put("vipPoint", vipPoint);
		msg.put("vipLevel", vipLevel);
		msg.put("diamond", diamond);

		msg.put("score", score);
		return msg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExpSum() {
		return expSum;
	}

	public void setExpSum(int expSum) {
		this.expSum = expSum;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public Date getEnergyUT() {
		return energyUT;
	}

	public void setEnergyUT(Date energyUT) {
		this.energyUT = energyUT;
	}

	public int getVipPoint() {
		return vipPoint;
	}

	public void setVipPoint(int vipPoint) {
		this.vipPoint = vipPoint;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public long getOnlineSum() {
		return onlineSum;
	}

	public void setOnlineSum(long onlineSum) {
		this.onlineSum = onlineSum;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public long getMaxBats() {
		return maxBats;
	}

	public void setMaxBats(long maxBats) {
		this.maxBats = maxBats;
	}

	public Recharge getCharge() {
		return charge;
	}

	public void setCharge(Recharge charge) {
		this.charge = charge;
	}

}
