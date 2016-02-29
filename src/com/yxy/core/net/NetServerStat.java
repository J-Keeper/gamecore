package com.yxy.core.net;

import org.apache.mina.core.service.IoServiceStatistics;

public class NetServerStat implements NetServerStatMBean {
	private IoServiceStatistics iss;

	public NetServerStat() {
	}

	public NetServerStat(IoServiceStatistics iss) {
		this.iss = iss;
	}

	public IoServiceStatistics getIss() {
		return this.iss;
	}

	public void setIss(IoServiceStatistics iss) {
		this.iss = iss;
	}

	/**
	 * 返回会话的累积数量(目前管理的会话数+关闭会话数)
	 */
	public long getCumulativeManagedSessionCount() {
		return this.iss.getCumulativeManagedSessionCount();
	}

	/**
	 * 此时正被管理的会话数量
	 */
	public int getLargestManagedSessionCount() {
		return this.iss.getLargestManagedSessionCount();
	}

	public double getLargestReadBytesThroughput() {
		return this.iss.getLargestReadBytesThroughput();
	}

	public double getLargestReadMessagesThroughput() {
		return this.iss.getLargestReadMessagesThroughput();
	}

	public double getLargestWrittenBytesThroughput() {
		return this.iss.getLargestWrittenBytesThroughput();
	}

	public double getLargestWrittenMessagesThroughput() {
		return this.iss.getLargestWrittenMessagesThroughput();
	}

	public long getLastIoTime() {
		return this.iss.getLastIoTime();
	}

	public long getLastReadTime() {
		return this.iss.getLastReadTime();
	}

	public long getLastWriteTime() {
		return this.iss.getLastWriteTime();
	}

	public long getReadBytes() {
		return this.iss.getReadBytes();
	}

	public double getReadBytesThroughput() {
		return this.iss.getReadBytesThroughput();
	}

	public long getReadMessages() {
		return this.iss.getReadMessages();
	}

	public double getReadMessagesThroughput() {
		return this.iss.getReadMessagesThroughput();
	}

	public int getScheduledWriteBytes() {
		return this.iss.getScheduledWriteBytes();
	}

	public int getScheduledWriteMessages() {
		return this.iss.getScheduledWriteMessages();
	}

	public int getThroughputCalculationInterval() {
		return this.iss.getThroughputCalculationInterval();
	}

	public long getThroughputCalculationIntervalInMillis() {
		return this.iss.getThroughputCalculationIntervalInMillis();
	}

	public long getWrittenBytes() {
		return this.iss.getWrittenBytes();
	}

	public double getWrittenBytesThroughput() {
		return this.iss.getWrittenBytesThroughput();
	}

	public long getWrittenMessages() {
		return this.iss.getWrittenMessages();
	}

	public double getWrittenMessagesThroughput() {
		return this.iss.getWrittenMessagesThroughput();
	}
}