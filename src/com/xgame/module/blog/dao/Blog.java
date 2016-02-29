package com.xgame.module.blog.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.alibaba.fastjson.JSONObject;
import com.xgame.module.avatar.dao.Avatar;
import com.xgame.module.blog.BlogOpt;

/**
 * 王牌空战玩家日志记录数据表
 * 
 * @since 2015年5月6日 下午9:01:29
 * @author YongXinYu
 */
@Table("logs_${cid}")
public class Blog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(auto = true)
	private int id;
	@Column
	private long aid;
	/** 角色名称 */
	@Column
	private String name;
	/** 操作代号 */
	@Column
	@ColDefine(width = 50)
	private BlogOpt opt;

	/** 1.玩家等级 */
	@Column
	private int level;
	/** 2.钻石 */
	@Column
	private long diamond;
	/** 3.体力 */
	@Column
	private int energy;
	/** 4.声望 */
	@Column
	private int prestige;
	/** 5.军功 */
	@Column
	private int honor;
	/** 6.经验 */
	@Column
	private int exp;

	/** 7.铁 */
	@Column
	private long iron;
	/** 8.石油 */
	@Column
	private long oil;
	/** 9.铝 */
	@Column
	private long aore;
	/** 10.钛 */
	@Column
	private long tore;
	/** 11.水晶 */
	@Column
	private long crystal;

	/** --------------- 资源产出 --------------- */
	/** 1.产出钻石 */
	@Column
	private int obtainDiamond;
	/** 2.产出能量 */
	@Column
	private int obtainEnergy;
	/** 3.产出声望 */
	@Column
	private int obtainPrestige;
	/** 4.产出军功 */
	@Column
	private int obtainHonor;
	/** 5.产出经验 */
	@Column
	private int obtainExp;
	/** 6.产出铁 */
	@Column
	private long obtainIron;
	/** 7.产出石油 */
	@Column
	private long obtainOil;
	/** 8.产出铝 */
	@Column
	private long obtainAore;
	/** 9.产出钛 */
	@Column
	private long obtainTore;
	/** 10.产出水晶 */
	@Column
	private long obtainCrystal;

	/** --------------- 资源消耗 --------------- */
	/** 1.消耗钻石 */
	@Column
	private int lostDiamond;
	/** 2.消耗能量 */
	@Column
	private int lostEnergy;
	/** 3.消耗声望 */
	@Column
	private int lostPrestige;
	/** 4.消耗军功 */
	@Column
	private int lostHonor;
	/** 5.消耗经验 */
	@Column
	private int lostExp;
	/** 6.消耗铁 */
	@Column
	private long lostIron;
	/** 7.消耗石油 */
	@Column
	private long lostOil;
	/** 8.消耗铝 */
	@Column
	private long lostAore;
	/** 9.消耗钛 */
	@Column
	private long lostTore;
	/** 10.消耗水晶 */
	@Column
	private long lostCrystal;

	/** --------------- 抽象道具物品产出与消耗 --------------- */
	/** 1.飞机产出与损毁 */
	@Column
	@ColDefine(width = 1024)
	private String obtainPlane;
	@Column
	@ColDefine(width = 1024)
	private String lostPlane;

	/** 2.配件的产出与分解 */
	@Column
	@ColDefine(width = 1024)
	private String obtainParts;
	@Column
	@ColDefine(width = 1024)
	private String lostParts;

	/** 3.英雄的产出与消耗 */
	@Column
	@ColDefine(width = 1024)
	private String obtainHero;
	@Column
	@ColDefine(width = 1024)
	private String lostHero;

	/** 4.普通道具的产出与消耗 */
	@Column
	@ColDefine(width = 2048)
	private String obtainItem;
	@Column
	@ColDefine(width = 2048)
	private String lostItem;

	/** 5.世界地图任务产生及消亡 */
	@Column
	@ColDefine(width = 1024)
	private String createTask;
	@Column
	@ColDefine(width = 1024)
	private String lostTask;

	/** 在线时间 */
	@Column
	private long onlineTime;

	/** 其他操作信息 */
	@Column
	@ColDefine(width = 2048)
	private String note;
	@Column
	private Date createTime;

	private JSONObject obtainPlaneJson = new JSONObject();
	private JSONObject lostPlaneJson = new JSONObject();

	private JSONObject obtainPartsJson = new JSONObject();
	private JSONObject lostPartsJson = new JSONObject();

	private JSONObject obtainHeroJson = new JSONObject();
	private JSONObject lostHeroJson = new JSONObject();

	private JSONObject obtainItemJson = new JSONObject();
	private JSONObject lostItemJson = new JSONObject();

	private JSONObject createTaskJson = new JSONObject();
	private JSONObject lostTaskJson = new JSONObject();

	private JSONObject noteJson = new JSONObject();

	public Blog() {
	}

	public Blog(Avatar avatar, BlogOpt opt) {
		this.aid = avatar.getId();
		this.name = avatar.getName();
		this.opt = opt;

	}

	public void note(String key, Object value) {
		noteJson.put(key, value);
		note = noteJson.toString();
	}

	public void note(Map<String, Object> notes) {
		noteJson.putAll(notes);
		note = noteJson.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BlogOpt getOpt() {
		return opt;
	}

	public void setOpt(BlogOpt opt) {
		this.opt = opt;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getDiamond() {
		return diamond;
	}

	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getPrestige() {
		return prestige;
	}

	public void setPrestige(int prestige) {
		this.prestige = prestige;
	}

	public int getHonor() {
		return honor;
	}

	public void setHonor(int honor) {
		this.honor = honor;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public long getIron() {
		return iron;
	}

	public void setIron(long iron) {
		this.iron = iron;
	}

	public long getOil() {
		return oil;
	}

	public void setOil(long oil) {
		this.oil = oil;
	}

	public long getAore() {
		return aore;
	}

	public void setAore(long aore) {
		this.aore = aore;
	}

	public long getTore() {
		return tore;
	}

	public void setTore(long tore) {
		this.tore = tore;
	}

	public long getCrystal() {
		return crystal;
	}

	public void setCrystal(long crystal) {
		this.crystal = crystal;
	}

	public int getObtainDiamond() {
		return obtainDiamond;
	}

	public void setObtainDiamond(int obtainDiamond) {
		this.obtainDiamond = obtainDiamond;
	}

	public int getObtainEnergy() {
		return obtainEnergy;
	}

	public void setObtainEnergy(int obtainEnergy) {
		this.obtainEnergy = obtainEnergy;
	}

	public int getObtainPrestige() {
		return obtainPrestige;
	}

	public void setObtainPrestige(int obtainPrestige) {
		this.obtainPrestige = obtainPrestige;
	}

	public int getObtainHonor() {
		return obtainHonor;
	}

	public void setObtainHonor(int obtainHonor) {
		this.obtainHonor = obtainHonor;
	}

	public int getObtainExp() {
		return obtainExp;
	}

	public void setObtainExp(int obtainExp) {
		this.obtainExp = obtainExp;
	}

	public long getObtainIron() {
		return obtainIron;
	}

	public void setObtainIron(long obtainIron) {
		this.obtainIron = obtainIron;
	}

	public long getObtainOil() {
		return obtainOil;
	}

	public void setObtainOil(long obtainOil) {
		this.obtainOil = obtainOil;
	}

	public long getObtainAore() {
		return obtainAore;
	}

	public void setObtainAore(long obtainAore) {
		this.obtainAore = obtainAore;
	}

	public long getObtainTore() {
		return obtainTore;
	}

	public void setObtainTore(long obtainTore) {
		this.obtainTore = obtainTore;
	}

	public long getObtainCrystal() {
		return obtainCrystal;
	}

	public void setObtainCrystal(long obtainCrystal) {
		this.obtainCrystal = obtainCrystal;
	}

	public int getLostDiamond() {
		return lostDiamond;
	}

	public void setLostDiamond(int lostDiamond) {
		this.lostDiamond = lostDiamond;
	}

	public int getLostEnergy() {
		return lostEnergy;
	}

	public void setLostEnergy(int lostEnergy) {
		this.lostEnergy = lostEnergy;
	}

	public int getLostPrestige() {
		return lostPrestige;
	}

	public void setLostPrestige(int lostPrestige) {
		this.lostPrestige = lostPrestige;
	}

	public int getLostHonor() {
		return lostHonor;
	}

	public void setLostHonor(int lostHonor) {
		this.lostHonor = lostHonor;
	}

	public int getLostExp() {
		return lostExp;
	}

	public void setLostExp(int lostExp) {
		this.lostExp = lostExp;
	}

	public long getLostIron() {
		return lostIron;
	}

	public void setLostIron(long lostIron) {
		this.lostIron = lostIron;
	}

	public long getLostOil() {
		return lostOil;
	}

	public void setLostOil(long lostOil) {
		this.lostOil = lostOil;
	}

	public long getLostAore() {
		return lostAore;
	}

	public void setLostAore(long lostAore) {
		this.lostAore = lostAore;
	}

	public long getLostTore() {
		return lostTore;
	}

	public void setLostTore(long lostTore) {
		this.lostTore = lostTore;
	}

	public long getLostCrystal() {
		return lostCrystal;
	}

	public void setLostCrystal(long lostCrystal) {
		this.lostCrystal = lostCrystal;
	}

	public String getObtainPlane() {
		return obtainPlane;
	}

	public void setObtainPlane(String obtainPlane) {
		this.obtainPlane = obtainPlane;
	}

	public String getLostPlane() {
		return lostPlane;
	}

	public void setLostPlane(String lostPlane) {
		this.lostPlane = lostPlane;
	}

	public String getObtainParts() {
		return obtainParts;
	}

	public void setObtainParts(String obtainParts) {
		this.obtainParts = obtainParts;
	}

	public String getLostParts() {
		return lostParts;
	}

	public void setLostParts(String lostParts) {
		this.lostParts = lostParts;
	}

	public String getObtainHero() {
		return obtainHero;
	}

	public void setObtainHero(String obtainHero) {
		this.obtainHero = obtainHero;
	}

	public String getLostHero() {
		return lostHero;
	}

	public void setLostHero(String lostHero) {
		this.lostHero = lostHero;
	}

	public String getObtainItem() {
		return obtainItem;
	}

	public void setObtainItem(String obtainItem) {
		this.obtainItem = obtainItem;
	}

	public String getLostItem() {
		return lostItem;
	}

	public void setLostItem(String lostItem) {
		this.lostItem = lostItem;
	}

	public String getCreateTask() {
		return createTask;
	}

	public void setCreateTask(String createTask) {
		this.createTask = createTask;
	}

	public String getLostTask() {
		return lostTask;
	}

	public void setLostTask(String lostTask) {
		this.lostTask = lostTask;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public JSONObject getObtainPlaneJson() {
		return obtainPlaneJson;
	}

	public void setObtainPlaneJson(JSONObject obtainPlaneJson) {
		this.obtainPlaneJson = obtainPlaneJson;
	}

	public JSONObject getLostPlaneJson() {
		return lostPlaneJson;
	}

	public void setLostPlaneJson(JSONObject lostPlaneJson) {
		this.lostPlaneJson = lostPlaneJson;
	}

	public JSONObject getObtainPartsJson() {
		return obtainPartsJson;
	}

	public void setObtainPartsJson(JSONObject obtainPartsJson) {
		this.obtainPartsJson = obtainPartsJson;
	}

	public JSONObject getLostPartsJson() {
		return lostPartsJson;
	}

	public void setLostPartsJson(JSONObject lostPartsJson) {
		this.lostPartsJson = lostPartsJson;
	}

	public JSONObject getObtainHeroJson() {
		return obtainHeroJson;
	}

	public void setObtainHeroJson(JSONObject obtainHeroJson) {
		this.obtainHeroJson = obtainHeroJson;
	}

	public JSONObject getLostHeroJson() {
		return lostHeroJson;
	}

	public void setLostHeroJson(JSONObject lostHeroJson) {
		this.lostHeroJson = lostHeroJson;
	}

	public JSONObject getObtainItemJson() {
		return obtainItemJson;
	}

	public void setObtainItemJson(JSONObject obtainItemJson) {
		this.obtainItemJson = obtainItemJson;
	}

	public JSONObject getLostItemJson() {
		return lostItemJson;
	}

	public void setLostItemJson(JSONObject lostItemJson) {
		this.lostItemJson = lostItemJson;
	}

	public JSONObject getCreateTaskJson() {
		return createTaskJson;
	}

	public void setCreateTaskJson(JSONObject createTaskJson) {
		this.createTaskJson = createTaskJson;
	}

	public JSONObject getLostTaskJson() {
		return lostTaskJson;
	}

	public void setLostTaskJson(JSONObject lostTaskJson) {
		this.lostTaskJson = lostTaskJson;
	}

	public JSONObject getNoteJson() {
		return noteJson;
	}

	public void setNoteJson(JSONObject noteJson) {
		this.noteJson = noteJson;
	}

	/**
	 * @return
	 */
	public int getTableCid() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(createTime);
		int woy = cal.get(Calendar.WEEK_OF_YEAR);
		return woy % 12;
	}

}