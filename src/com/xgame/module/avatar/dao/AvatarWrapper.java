/** 广州哇宝信息技术有限公司 */
package com.xgame.module.avatar.dao;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.xgame.util.ObjectUtil;

/**
 * 角色系统持久化数据
 * 
 * @since 2015年2月23日 下午5:47:49
 * @author YongXinYu
 */
@Table("avatar")
public class AvatarWrapper {
	/** 1.aid */
	@Id(auto = false)
	private long id;
	/** 2.关联的账号 **/
	@Column
	private String account;
	/** 3.名字 **/
	@Column
	private String name;
	/** 4.性别 (1-男,2-女) */
	@Column
	private byte gender;
	/** 5.主角形象Id */
	@Column
	private String icon;

	/** 6.当前经验 **/
	@Column
	private int exp;
	/** 7.当前等级 **/
	@Column
	private int level;
	@Column
	/** 8.历史总经验 **/
	private int expSum;

	/** 9.能量 **/
	@Column
	private int energy;
	/** 10.最近一次能量更新时间 */
	@Column
	private Date energyUT;

	/** 11.VIP积分 **/
	@Column
	private int vipPoint;
	/** 12.VIP等级 **/
	@Column
	private int vipLevel;
	/** 13.钻石 **/
	@Column
	private int diamond;

	/** 14.是否在线 */
	@Column
	private boolean isOnline;
	/** 15.最近一次登陆时间 */
	@Column
	private Date loginTime;
	/** 16.最近一次退出 */
	@Column
	private Date logoutTime;
	/** 17. 玩家总在线时间 秒 */
	@Column
	private long onlineSum;
	/** 18.角色创建时间 */
	@Column
	private Date createDate;
	/** 19.战力 */
	@Column
	private long score;
	/** 20.历史最高战力 */
	@Column
	private long maxBats;

	/** 充值数据封装 */
	@Column
	public byte[] wd;

	public AvatarWrapper() {
	}

	public AvatarWrapper(Avatar avatar) {
		this.id = avatar.getId();
		this.account = avatar.getAccount();
		this.name = avatar.getName();
		this.gender = avatar.getGender();
		this.icon = avatar.getIcon();

		this.exp = avatar.getExp();
		this.level = avatar.getLevel();
		this.expSum = avatar.getExpSum();

		this.energy = avatar.getEnergy();
		this.energyUT = avatar.getEnergyUT();

		this.vipPoint = avatar.getVipPoint();
		this.vipLevel = avatar.getVipLevel();
		this.diamond = avatar.getDiamond();

		this.loginTime = avatar.getLoginTime();
		this.logoutTime = avatar.getLogoutTime();
		this.onlineSum = avatar.getOnlineSum();

		this.createDate = avatar.getCreateDate();
		this.wd = ObjectUtil.fromObject(avatar.getCharge());

		this.score = avatar.getScore();
		this.maxBats = avatar.getMaxBats();
	}

	public Avatar unWrap() {
		Avatar avatar = new Avatar();
		avatar.setId(this.id);
		avatar.setAccount(this.account);
		avatar.setName(this.name);
		avatar.setGender(this.gender);
		avatar.setIcon(this.icon);

		avatar.setLevel(this.level);
		avatar.setExp(this.exp);
		avatar.setExpSum(this.expSum);
		avatar.setEnergy(this.energy);
		avatar.setEnergyUT(this.energyUT);

		avatar.setVipPoint(this.vipPoint);
		avatar.setVipLevel(this.vipLevel);
		avatar.setDiamond(this.diamond);

		avatar.setLoginTime(this.loginTime);
		avatar.setLogoutTime(this.logoutTime);
		avatar.setOnlineSum(this.onlineSum);
		avatar.setCreateDate(this.createDate);

		Recharge rc = ObjectUtil.toObject(Recharge.class, wd);
		rc.check();
		avatar.setCharge(rc);
		avatar.setScore(this.score);
		avatar.setMaxBats(this.maxBats);
		return avatar;
	}
}
