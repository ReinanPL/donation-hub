package com.compass.Exception;

public class LimitReachedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LimitReachedException(String msg) {
        super(msg);
    }

}
