package com.yxy.core.net;

/**
 * 
 * @Description 反应服务器状态的接口
 * @author Yongxinyu
 * @date 2014年12月24日 下午2:12:58
 */
public interface NetServerStatMBean {
	public abstract long getCumulativeManagedSessionCount();

	/**
	 * 最大与客户端建立的Session个数
	 * 
	 * @return
	 */
	public abstract int getLargestManagedSessionCount();

	/**
	 * 最大数据接收流量/s
	 * 
	 * @return
	 */
	public abstract double getLargestReadBytesThroughput();

	/**
	 * 最大消息接收数量/s
	 * 
	 * @return
	 */
	public abstract double getLargestReadMessagesThroughput();

	/**
	 * 最大数据写出数量/s
	 * 
	 * @return
	 */
	public abstract double getLargestWrittenBytesThroughput();

	/**
	 * 最大消息写出数量/s
	 * 
	 * @return
	 */
	public abstract double getLargestWrittenMessagesThroughput();

	/**
	 * 最近一次与内存进行io的时间
	 * 
	 * @return
	 */
	public abstract long getLastIoTime();

	/**
	 * 最近一次从套接口读数据时间
	 * 
	 * @return
	 */
	public abstract long getLastReadTime();

	/**
	 * 最近一次从套接口写数据时间
	 * 
	 * @return
	 */
	public abstract long getLastWriteTime();

	/**
	 * 目前获取读取的字节数量
	 * 
	 * @return
	 */
	public abstract long getReadBytes();

	/**
	 * 目前获取读取的字节数量/s
	 * 
	 * @return
	 */
	public abstract double getReadBytesThroughput();

	/**
	 * 目前读取的消息数量
	 * 
	 * @return
	 */
	public abstract long getReadMessages();

	/**
	 * 目前读取的消息数量/s
	 * 
	 * @return
	 */
	public abstract double getReadMessagesThroughput();

	/**
	 * 内存中等待套接口写出的字节数量
	 * 
	 * @return
	 */
	public abstract int getScheduledWriteBytes();

	/**
	 * 内存中等待套接口写出的消息数量
	 * 
	 * @return
	 */
	public abstract int getScheduledWriteMessages();

	/**
	 * 时间间隔吞吐量
	 * 
	 * @return
	 */
	public abstract int getThroughputCalculationInterval();

	/**
	 * 每秒吞吐量
	 * 
	 * @return
	 */
	public abstract long getThroughputCalculationIntervalInMillis();

	/**
	 * 已经写出的字节数
	 * 
	 * @return
	 */
	public abstract long getWrittenBytes();

	/**
	 * 已经写出的字节数的总吞吐量
	 * 
	 * @return
	 */
	public abstract double getWrittenBytesThroughput();

	/**
	 * 已经写出的消息数
	 * 
	 * @return
	 */
	public abstract long getWrittenMessages();

	/**
	 * 已经写出的消息数的吞吐量
	 * 
	 * @return
	 */
	public abstract double getWrittenMessagesThroughput();
}