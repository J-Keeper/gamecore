package com.yxy.core.framework;

import com.yxy.core.net.codec.Message;

/**
 * @Description: 分发器接口
 * @author YongXinYu
 * @date 2015年8月6日 下午4:24:26
 */
public interface IDispatcher {
	public void register(Object paramObject);

	public void dispatcher(Message paramMessage);

	public boolean add(Message paramMessage);

	public void stop();
}