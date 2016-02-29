package com.yxy.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CallBack {
	private final Object target;
	private final Method method;

	public CallBack(Object target, Method method) {
		this.target = target;
		this.method = method;
	}

	public Object handle(Object[] args) throws InvocationTargetException {
		try {
			return method.invoke(target, args);
		} catch (IllegalArgumentException e) {
			throw new Error("Method rejected target/argument: "
					+ Arrays.toString(args), e);
		} catch (IllegalAccessException e) {
			throw new Error("Method became inaccessible: "
					+ Arrays.toString(args), e);
		} catch (InvocationTargetException e) {
			if ((e.getCause() instanceof Error)) {
				throw ((Error) e.getCause());
			}
			throw e;
		}
	}

	public Method getMethod() {
		return this.method;
	}

	public Object getTarget() {
		return this.target;
	}

	public String toString() {
		return "[wrapper " + this.method + "]";
	}

	public int hashCode() {
		int PRIME = 31;
		return (PRIME + method.hashCode()) * 31
				+ System.identityHashCode(target);
	}

	public boolean equals(Object obj) {
		if ((obj instanceof CallBack)) {
			CallBack that = (CallBack) obj;
			return (obj == that.target) && (method.equals(that.method));
		}
		return false;
	}
}