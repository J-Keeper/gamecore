package com.xgame.packet;

/**
 * 数据自检,用来初始化一些扩展字段
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:32:44
 */
public interface DataCheck {

	/** 数据检查和初始化扩展字段(相对原始构造方法) */
	public void check();
}
