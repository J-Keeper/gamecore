package com.yxy.core.sch.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.sch.Job;
import com.yxy.core.sch.Trigger;

public class SimpleTrigger extends Trigger {

	private static final Logger log = LoggerFactory
			.getLogger(SimpleTrigger.class);

	private int repeatCount = REPEAT_INDEFINITELY;

	private int repeatInterval;
	private Date endTime;

	public SimpleTrigger(String name, Date startTime, int repeatCount,
			int repeatInterval, Date endTime, Job task) {
		setName(name);
		this.repeatCount = repeatCount;
		this.repeatInterval = repeatInterval;
		this.endTime = endTime;
		if (startTime.getTime() > System.currentTimeMillis()) {
			setNextTime(startTime.getTime());
			setTask(task);
		} else {
			setCancel(true);
			log.warn("已取消一个开始于过去时间的任务:" + name);
		}
	}

	public SimpleTrigger(String name, Date startTime, Job task) {
		this(name, startTime, 1, 32767, task);
	}

	public SimpleTrigger(String name, Date startTime, int repeatCount,
			int repeatInterval, Job task) {
		this(name, startTime, repeatCount, repeatInterval, null, task);
	}

	public SimpleTrigger(String name, Date startTime, int repeatInterval,
			Job task) {
		this(name, startTime, REPEAT_INDEFINITELY, repeatInterval, null, task);
	}

	public SimpleTrigger(String name, Date startTime, Date endStime,
			int repeatInterval, Job job) {
		this(name, startTime, 0, repeatInterval, endStime, job);
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getRepeatCount() {
		return this.repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public int getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(int repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public void updateNextTime(long curr) {
		setNextTime(curr + getRepeatInterval());
		if (getRepeatCount() == Trigger.REPEAT_INDEFINITELY) {
			return;
		}
		if (getEndTime() != null) {
			if (getNextTime() > getEndTime().getTime())
				setComplete(true);
		} else if (getTriggerCount() >= getRepeatCount()) {// 已经执行完指定的次数
			setComplete(true);
		}
	}
}
