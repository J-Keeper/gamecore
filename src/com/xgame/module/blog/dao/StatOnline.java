package com.xgame.module.blog.dao;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("statonline")
@TableIndexes({ @Index(name = "idx_statTime", fields = { "statTime" }) })
public class StatOnline {
	@Id
	private int id;
	@Column
	private Date statTime;
	@Column
	private int onlineNum;
	@Column
	private int sessionNum;
	@Column
	private int iosNum;
	@Column
	private int androidNum;
	@Column
	private int aPadNum;// android pad 安桌平板
	@Column
	private int iPadNum;

	public StatOnline() {
	}

	public StatOnline(int onlineNum, int sessionNum) {
		this.statTime = new Date();
		this.onlineNum = onlineNum;
		this.sessionNum = sessionNum;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStatTime() {
		return this.statTime;
	}

	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}

	public int getOnlineNum() {
		return this.onlineNum;
	}

	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}

	public int getSessionNum() {
		return this.sessionNum;
	}

	public void setSessionNum(int sessionNum) {
		this.sessionNum = sessionNum;
	}

	public int getIosNum() {
		return iosNum;
	}

	public void setIosNum(int iosNum) {
		this.iosNum = iosNum;
	}

	public int getAndroidNum() {
		return androidNum;
	}

	public void setAndroidNum(int androidNum) {
		this.androidNum = androidNum;
	}

	public int getaPadNum() {
		return aPadNum;
	}

	public void setaPadNum(int aPadNum) {
		this.aPadNum = aPadNum;
	}

	public int getiPadNum() {
		return iPadNum;
	}

	public void setiPadNum(int iPadNum) {
		this.iPadNum = iPadNum;
	}

}