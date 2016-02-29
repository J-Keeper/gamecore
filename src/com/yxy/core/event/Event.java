package com.yxy.core.event;

import java.io.Serializable;

/**
 * @Description:游戏内事件的实体
 * @author YongXinYu
 * @date 2015年8月5日 下午9:59:03
 */
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	private int type;
	private Object source;
	private Object data;

	public Event(int type, Object source) {
		this.source = source;
		this.type = type;
	}

	public Event(int type, Object source, Object data) {
		this(type, source);
		this.data = data;
	}

	public int getType() {
		return this.type;
	}

	public Object getSource() {
		return this.source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String toString() {
		return "Event [type=" + this.type + ", source=" + this.source
				+ ", data=" + this.data + "]";
	}
}
