package com.kh.spring.member.exception;

//RuntimeException을 상속하여 예외처리 강제화x
public class MemberException extends RuntimeException {

	public MemberException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MemberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
