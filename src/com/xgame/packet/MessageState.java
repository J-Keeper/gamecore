package com.xgame.packet;

/**
 * 消息状态代码,范围[200, Int.MAX],同一个消息可以对应多个不同的代码,状态码可以在消息间重用<br>
 * <strong>注意:请按照顺序编写状态码<br>
 * 200以下为服务器内置状态码,各系统模块不需要编写! </strong>
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:31:56
 */
public class MessageState {
	/** 成功, 消息默认值状态 */
	public static final int SUC = 200;
	/** 处理失败, 服务器没有给出具体原因 */
	public static final int FAIL = 201;
	/** 禁止发送该消息 */
	public static final int FORBID = 202;
	/** 角色名长度有误 */
	public static final int LENGTH_ERR = 203;
	/** 账号已在别处登录 */
	public static final int LOGIN_OTHER = 204;
	/** 帐号或密码错误 */
	public static final int PASS_ERROR = 205;
	/** 角色已经存在 */
	public static final int DUP_CREATE = 206;
	/** 包含非法字 */
	public static final int ILLEGAL_WORD = 207;
	/** 参数错误 */
	public static final int PARAMS_ERR = 208;
	/** 角色不存在 */
	public static final int NEED_AVATAR = 209;
	/** 体力不够 */
	public static final int ENERGY_LACK = 210;
	/** 砖石不够 */
	public static final int DIAMOND_LACK = 211;
}