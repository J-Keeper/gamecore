package com.yxy.core.sch.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yxy.core.sch.Job;
import com.yxy.core.sch.Trigger;

public class WeeklyTrigger extends Trigger {

	private List<Calendar> calList;
	private int index = 0;

	public WeeklyTrigger(String name, List<Calendar> calList, Job job) {
		setName(name);
		setTask(job);
		setCalList(passCalList(calList));
		setNextTime(calList.get(0).getTimeInMillis());
	}

	private List<Calendar> passCalList(List<Calendar> calList) {
		Calendar curr = Calendar.getInstance();
		for (Calendar cal : calList) {
			if (cal.compareTo(curr) < 0) {
				cal.add(3, 1);
			}
		}
		calSort(calList);
		return calList;
	}

	public WeeklyTrigger(String name, int hour, int minute, int[] dayOfWeek,
			Job job) {
		if ((dayOfWeek == null) || (dayOfWeek.length == 0)) {
			throw new IllegalArgumentException("day of week must be set!");
		}
		List<Calendar> calList = new ArrayList<>();
		Calendar curr = Calendar.getInstance();
		for (int day : dayOfWeek) {
			Calendar cal = Calendar.getInstance();
			cal.set(7, day);
			cal.set(11, hour);
			cal.set(12, minute);
			cal.set(13, 0);
			calList.add(cal);
			if (cal.compareTo(curr) < 0) {
				cal.add(3, 1);
			}
		}
		setName(name);
		setTask(job);
		calSort(calList);
		setCalList(calList);
		setNextTime(((Calendar) calList.get(0)).getTimeInMillis());
	}

	private void calSort(List<Calendar> calList) {
		Collections.sort(calList, new Comparator<Calendar>() {
			public int compare(Calendar o1, Calendar o2) {
				return o1.compareTo(o2);
			}
		});
	}

	public List<Calendar> getCalList() {
		return calList;
	}

	public void setCalList(List<Calendar> calList) {
		this.calList = calList;
	}

	public void updateNextTime(long curr) {
		Calendar cal = calList.get(index);
		cal.add(3, 1);
		index = ((index + 1) % calList.size());
		setNextTime(calList.get(index).getTimeInMillis());
	}

}
