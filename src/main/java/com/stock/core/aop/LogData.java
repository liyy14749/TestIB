package com.stock.core.aop;

import java.util.Map;

public class LogData {
	private Object req;

	private Object resp;

	private Map<String, Long> api;

	private String ip;

	private long time;
	
	private String uri;

	private Map<String, String> header;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Object getReq() {
		return this.req;
	}

	public void setReq(final Object req) {
		this.req = req;
	}

	public Object getResp() {
		return this.resp;
	}

	public void setResp(final Object resp) {
		this.resp = resp;
	}

	public Map<String, Long> getApi() {
		return this.api;
	}

	public void setApi(final Map<String, Long> api) {
		this.api = api;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}

	public long getTime() {
		return this.time;
	}

	public void setTime(final long time) {
		this.time = time;
	}

	public Map<String, String> getHeader() {
		return this.header;
	}

	public void setHeader(final Map<String, String> header) {
		this.header = header;
	}

}
