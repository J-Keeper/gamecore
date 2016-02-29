package com.yxy.core.msg.adp;

public class AdaptorException extends Exception {
	private static final long serialVersionUID = 1L;

	public AdaptorException(String message) {
		super(message);
	}

	public AdaptorException(String message, Throwable cause) {
		super(message, cause);
	}

}