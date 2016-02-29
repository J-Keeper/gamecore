package com.yxy.core.msg.adp;

import java.lang.reflect.Method;

import com.yxy.core.net.codec.Message;

public interface IAdaptor {
	public void init(Method paramMethod) throws AdaptorException;

	public Object[] adaptorMsg(Message paramMessage) throws AdaptorException,
			IllegalAccessException;
}