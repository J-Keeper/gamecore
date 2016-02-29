package com.xgame.event;

/**
 * 事件类型
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:30:03
 */
public final class EventType {
	/** 上线 */
	public static final int ONLINE = 1;
	/** 上线后 */
	public static final int ONLINEAFTER = 2;
	/** 下线前 */
	public static final int OFFLIEBEFORE = 3;
	/** 下线 */
	public static final int OFFLINE = 4;
	/** 角色升级 */
	public static final int AVA_LEVELUP = 5;
	/** 创建角色 */
	public static final int CREATE_AVATAR = 6;
}