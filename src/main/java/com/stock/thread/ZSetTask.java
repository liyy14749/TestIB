package com.stock.thread;

import lombok.Data;

@Data
public class ZSetTask extends MsgTask {
	private long time;
	private Object object;

	public ZSetTask(){
	}
	public ZSetTask(String key, Integer symbolId, Object object, Long time){
		this.type = 2;
		this.redisKey = key;
		this.symbolId = symbolId;
		this.time = time;
		this.object = object;
	}
}
