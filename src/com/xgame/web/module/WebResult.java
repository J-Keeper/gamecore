package com.xgame.web.module;

/**
 * @author yaowenhao
 * @date 2014年8月29日 下午6:14:52
 */
public class WebResult {
	public final static int SUC = 0; // 缺省值
	public final static int AVATAR_OFFLINE = 1; // 玩家不在线
	public final static int AVATAR_NULL = 2; // 玩家不存在
	public final static int ERROR_PWD = 3; // 密码错误
	public final static int ERROR_LV = 4; // 等级错误
	public final static int ITEM_LACK = 5; // 物品数量不够
	public final static int RELOAD_FAIL = 6; // 加载数据失败
	public final static int RECHARGE_FAIL = 7; // 冲值失败
	public final static int GM_REPLY_FALL = 8;
	public final static int GM_REWARD_FALL = 9;
	public final static int PARAMS_NULL = 10;// 参数提交有误
	public final static int INFO_NULL = 11;// 查询信息为空
	public final static int RECHARGE_ALREADY_DELIVER = 12; // 订单已经发货
	// gm回复内容长度
	public final static int GM_REWARD_LENGTH = 300;
	private int s;
	private Object r;

	public WebResult() {
	}

	public WebResult(int state, Object data) {
		super();
		this.s = state;
		this.r = data;
	}

	public int getState() {
		return s;
	}

	public void setState(int state) {
		this.s = state;
	}

	public Object getData() {
		return r;
	}

	public void setData(Object data) {
		this.r = data;
	}

}
