package com.xgame.packet;


/**
 * 消息协议类型,常用成对的正负数表示<br>
 * 一般客户端请求为正数,如XXX_REQ(请求)<br>
 * 服务器下发为负数XXX_RES(响应),XXX_PUSH(推送)<br>
 * 建议命名方式: 需要操作的数据类+动作+类型, 如 AVATAR_CREATE_REQ表示角色创建请求<br>
 * <strong>注意:请按照顺序编写消息号<strong>
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:31:01
 */
public class MessageType {
	public static final short TEST = 0;
	public static final short SERVER_REGISTER_REQ = 10;
	public static final short SERVER_REGISTER_RES = -10;

	public static final short GLOABAL_EVENT = 11;
	public static final short KICK_TOPIC = 12;
	public static final short BRODCAST = 13;

	public static final short RECHARGE = 14;

	/** 下线 (内部已用) */
	public static final short LOGOUT_REQ = 101;
	public static final short LOGOUT_RES = -101;
	/** 登陆 (内部已用) */
	public static final short LOGIN_REQ = 102;
	public static final short LOGIN_RES = -102;

	/*********************** 角色相关的协议 2000-2099 **************************/
	/** 创建Avatar */
	public static final short AVATAR_CREATE_REQ = 2000;
	public static final short AVATAR_CREATE_RES = -2000;
	/** 加载Avatar */
	public static final short AVATAR_LOAD_REQ = 2001;
	public static final short AVATAR_LOAD_RES = -2001;

	/** 请求能量恢复 */
	public static final short AVATAR_ENERGYRECV_REQ = 2002;
	public static final short AVATAR_ENERGYRECV_RES = -2002;

	/** 角色属性更新推送 */
	public static final short AVATAR_UPDATE_PUSH = -2003;

}