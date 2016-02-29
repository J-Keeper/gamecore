package com.xgame.module.avatar.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.xgame.packet.DataCheck;
import com.xgame.packet.MessageSerializable;

public class Recharge implements Serializable, MessageSerializable, DataCheck {
	private static final long serialVersionUID = 1L;

	private int sumMoney;
	private int lastMoney;
	private Date lastChargeDate;

	@Override
	public Map<String, Object> toMsg() {
		return null;
	}

	public int getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(int sumMoney) {
		this.sumMoney = sumMoney;
	}

	public int getLastMoney() {
		return lastMoney;
	}

	public void setLastMoney(int lastMoney) {
		this.lastMoney = lastMoney;
	}

	public Date getLastChargeDate() {
		return lastChargeDate;
	}

	public void setLastChargeDate(Date lastChargeDate) {
		this.lastChargeDate = lastChargeDate;
	}

	@Override
	public void check() {
		System.err.println("need data check?");
	}

}
