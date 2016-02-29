package com.xgame.module.blog.dao;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 服务器启停记录
 * 
 * @author yaowenhao
 * @date 2014年10月11日 上午9:26:00
 */
@Table("startstop")
public class StartStop {

	@Id(auto = true)
	private int id;

	@Column
	private Date startTime;

	@Column
	private Date stopTime;

	@Column
	private String note;

	public StartStop() {
	}

	public StartStop(Date date, String note) {
		this.startTime = date;
		this.note = note;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return this.stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}