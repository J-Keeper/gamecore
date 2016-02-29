package com.yxy.core.net.filter;

import java.io.Serializable;

/**
 * @Description: 消息状态统计
 * @author YongXinYu
 * @date 2015年8月6日 下午7:31:08
 */
public class MessageStat implements Serializable {
	private static final long serialVersionUID = 1L;
	private short messageType;
	private long count;
	private long totalSize;
	private int type;
	private int avgSize;

	public MessageStat(short messageType, long size, int type) {
		this.messageType = messageType;
		this.count = 1L;
		this.totalSize = size;
		this.type = type;
	}

	/**
	 * 添加状态消息，同类型的状态消息进行合并
	 * 
	 * @param stat
	 *            MessageStat对象
	 * @return
	 */
	public MessageStat add(MessageStat stat) {
		if (this.type == stat.type) {
			this.count += stat.getCount();
			this.totalSize += stat.getTotalSize();
			return this;
		}
		return stat;
	}

	public short getMessageType() {
		return this.messageType;
	}

	public void setMessageType(short messageType) {
		this.messageType = messageType;
	}

	public long getCount() {
		return this.count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTotalSize() {
		return this.totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAvgSize() {
		this.avgSize = (int) (this.totalSize * 1.0D / this.count);
		return this.avgSize;
	}

	/**
	 * 更新平均尺寸
	 * 
	 * @return
	 */
	public int updateAvgSize() {
		this.avgSize = getAvgSize();
		return this.avgSize;
	}
}