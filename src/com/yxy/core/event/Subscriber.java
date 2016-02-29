package com.yxy.core.event;

import java.lang.reflect.Method;

import com.yxy.core.CallBack;

public class Subscriber extends CallBack {
	public Subscriber(Object target, Method method) {
		super(target, method);
	}

}