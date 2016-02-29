package com.yxy.core.msg;

import java.lang.reflect.Method;

import com.yxy.core.CallBack;
import com.yxy.core.msg.adp.Adaptor;
import com.yxy.core.msg.adp.AdaptorException;
import com.yxy.core.msg.adp.IAdaptor;
import com.yxy.core.net.codec.Message;

public class MessageCallBack extends CallBack {
	private boolean isAvatar = true;
	private IAdaptor adaptor;

	public MessageCallBack(Object obj, Method method, boolean isAvatar)
			throws AdaptorException {
		super(obj, method);
		this.isAvatar = isAvatar;
		this.adaptor = new Adaptor();
		this.adaptor.init(method);
	}

	public boolean isAvatar() {
		return this.isAvatar;
	}

	public void setAvatar(boolean isAvatar) {
		this.isAvatar = isAvatar;
	}

	public IAdaptor getAdaptor() {
		return this.adaptor;
	}

	public void setAdaptor(IAdaptor adaptor) {
		this.adaptor = adaptor;
	}

	public Object handle2(Message req) throws Exception {
		IAdaptor adp = getAdaptor();
		Object[] parms = adp.adaptorMsg(req);
		return handle(parms);
	}
}