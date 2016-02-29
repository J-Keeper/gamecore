package com.xgame.module.common;

import java.io.Serializable;

/**
 * 游戏角色相关的基本抽象资源
 * 
 * @since 2015年2月26日 下午4:59:15
 * @author YongXinYu
 */
public class Money implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 能量(可加减) */
	public static final short energy = 1;
	/** 钻石 (可加减) */
	public static final short diamond = 2;
	/** 声望 (只支持增加) */
	public static final short prestige = 3;
	/** 军功(只支持增加) */
	public static final short honor = 4;
	/** 经验(只支持增加) */
	public static final short exp = 5;

	private short type;
	private int value;

	public Money() {
	}

	public Money(short type, int value) {
		super();
		this.type = type;
		this.value = value;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Resource [type=" + type + ", value=" + value + "]";
	}
}
