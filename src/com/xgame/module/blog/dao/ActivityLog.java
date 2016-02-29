package com.xgame.module.blog.dao;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 活动相关的日志记录
 * 
 * @since 2015年5月14日 上午11:02:27
 * @author LSQ
 */
@Table("activitylog")
public class ActivityLog {
	@Id(auto = true)
	private int id;
	/** 活动ID */
	@Column
	private int actid;
	/** 活动类型 */
	@Column
	private int type;
	/** 活动时间 */
	@Column
	private Date actDate;
	/** 参与人数 */
	@Column
	private int acAvaCount;
	/** 符合条件人数 */
	@Column
	private int avaCount;
	/** 参与联盟数 */
	@Column
	private int acLeaCount;
	/** 符合联盟数 */
	@Column
	private int leaCount;

	public ActivityLog() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActid() {
		return actid;
	}

	public void setActid(int actid) {
		this.actid = actid;
	}

	public Date getActDate() {
		return actDate;
	}

	public void setActDate(Date actDate) {
		this.actDate = actDate;
	}

	public int getAcAvaCount() {
		return acAvaCount;
	}

	public void setAcAvaCount(int acAvaCount) {
		this.acAvaCount = acAvaCount;
	}

	public int getAvaCount() {
		return avaCount;
	}

	public void setAvaCount(int avaCount) {
		this.avaCount = avaCount;
	}

	public int getAcLeaCount() {
		return acLeaCount;
	}

	public void setAcLeaCount(int acLeaCount) {
		this.acLeaCount = acLeaCount;
	}

	public int getLeaCount() {
		return leaCount;
	}

	public void setLeaCount(int leaCount) {
		this.leaCount = leaCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}