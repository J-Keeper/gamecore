package com.yxy.core.sch;

/**
 * 定时器要执行的一个任务
 * 
 * @author YXY
 * @date 2015年9月10日 下午2:51:09
 */
public interface Job {
	public void execute(Trigger t);
}
