package com.yxy.core.sch.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.yxy.core.sch.Job;
import com.yxy.core.sch.Trigger;

public class DailyTrigger extends Trigger {

	private List<Calendar> calList;
	private int index = 0;

	public DailyTrigger(String name, String expression, Job job) {
		setName(name);
		setTask(job);
		setCalList(passTimeExp(expression));
		setNextTime(calList.get(0).getTimeInMillis());
	}

	public DailyTrigger(String name, List<Calendar> calList, Job job) {
		setName(name);
		setTask(job);
		setCalList(passCalList(calList));
		setNextTime(calList.get(0).getTimeInMillis());
	}

	private List<Calendar> passCalList(List<Calendar> calList) {
		Calendar curr = Calendar.getInstance();
		for (Calendar cal : calList) {
			if (cal.compareTo(curr) < 0) {
				cal.add(5, 1);
			}
		}
		calSort(calList);
		return calList;
	}

	private List<Calendar> passTimeExp(String expression) {
		List<Calendar> calList = new ArrayList<>();
		String[] array = expression.trim().split(";");
		Calendar curr = Calendar.getInstance();
		for (String hourMin : array)
			if (!hourMin.isEmpty()) {
				String[] time = hourMin.split(":");
				int hour = Integer.parseInt(time[0].trim());
				int minute = 0;
				if (time.length > 1) {
					minute = Integer.parseInt(time[1].trim());
				}
				int second = 0;
				if (time.length > 2) {
					second = Integer.parseInt(time[2].trim());
				}
				Calendar cal = Calendar.getInstance();
				cal.set(11, hour);
				cal.set(12, minute);
				cal.set(13, second);
				if (cal.compareTo(curr) < 0) {
					cal.add(5, 1);
				}
				calList.add(cal);
			}
		calSort(calList);
		return calList;
	}

	private void calSort(List<Calendar> calList) {
		Collections.sort(calList,
				(Calendar o1, Calendar o2) -> (o1).compareTo(o2));
	}

	public void updateNextTime(long curr) {
		Calendar cal = calList.get(index);
		cal.add(5, 1);
		index = ((index + 1) % calList.size());
		setNextTime(calList.get(index).getTimeInMillis());
	}

	public List<Calendar> getCalList() {
		return this.calList;
	}

	public void setCalList(List<Calendar> calList) {
		this.calList = calList;
	}

}
