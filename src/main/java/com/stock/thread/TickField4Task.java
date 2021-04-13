package com.stock.thread;

import lombok.Data;

@Data
public class TickField4Task extends MsgTask {
	private Object value;

	public TickField4Task(){
	}

	public TickField4Task(String key, Integer symbolId, Object value){
		this.type = 3;
		this.redisKey = key;
		this.symbolId = symbolId;
		this.value = value;
	}
}
