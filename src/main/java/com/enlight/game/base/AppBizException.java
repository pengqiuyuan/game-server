package com.enlight.game.base;

import java.io.PrintWriter;

public class AppBizException extends Exception {

	private static final long serialVersionUID = 1L;
	/**
     * 错误码
     */
    private String code;
    /**
     * 参数信息
     */
	private Object[] args;
	
	public AppBizException(String msg){
		super(msg);
	}

	public AppBizException(String code, String msg) {
		super(msg);
		this.code = code;
	}

	public AppBizException(String code, String msg, Throwable cause) {
		super(msg, cause);
		this.code = code;
	}

	public AppBizException(String code, Object[] args, String msg) {
        this(code, msg);
		this.args = args;
	}

	public AppBizException(String code, Object[] args, String msg, Throwable cause) {
		this(code, msg, cause);
		this.args = args;
	}
	public AppBizException(String code, Object[] args, String msg, Throwable cause,String source) {
        this(code, msg, cause);
	}
	public AppBizException(String code, Object[] args, String msg, Throwable cause,String source,int operLevel) {
        this(code, args, msg, cause, source);
	}
	public AppBizException(Throwable cause) {
		super(cause);
	}



	public String getCode() {
		return code;
	}

	public Object[] getArgs() {
		return args;
	}
	@Override
	public void printStackTrace(PrintWriter s) {
		 synchronized (s) {
	            s.println(this);
	     }
	}
}
