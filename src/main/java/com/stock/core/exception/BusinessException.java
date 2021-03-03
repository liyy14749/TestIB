package com.stock.core.exception;

import java.util.HashMap;
import java.util.Map;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;
	private String message;
	private Map<String, Object> params;

	public BusinessException() {
		super();
	}

	public BusinessException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BusinessException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BusinessException(int code, String message, Map<String, Object> params) {
		super();
		this.code = code;
		this.message = message;
		this.params = params;
	}
	
	public BusinessException(final Throwable cause) {
		super(cause);
	}

	public BusinessException(final int code) {
		super();
		this.code = code;
	}

	public BusinessException(final int code, final String message) {
		super();
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return "{\"code\":" + this.code + ", \"message\":\"" + this.getMessage() + "\"}";
	}

	public int getCode() {
		return this.code;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(final Map<String, Object> params) {
		this.params = params;
	}

	public BusinessException addParams(final String name, final String value) {
		if (this.params == null) {
			this.params = new HashMap<>();
		}
		this.params.put(name, value);
		return this;
	}

	@Override
	public String getMessage() {
		if (this.message == null) {
			String text = MessageLocalization.getString(this.code + "");
			/*			if (this.params != null) {
							for (String name : this.params.keySet()) {
								text = text.replaceAll("\\{" + name + "\\}", this.params.get(name));
							}
						}
			*/			return text;
		}
		return this.message;
	}

}
