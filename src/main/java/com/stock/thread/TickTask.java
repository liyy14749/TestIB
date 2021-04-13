package com.stock.thread;

import lombok.Data;

@Data
public class TickTask extends MsgTask {
	private String[] field;
	private Object[] value;

	public TickTask(){
	}

	public TickTask(String key,Integer symbolId,String field,Object value){
		this.type = 1;
		this.redisKey = key;
		this.symbolId = symbolId;
		this.field = new String[]{field};
		this.value = new Object[]{value};
	}

	public TickTask(String key,Integer symbolId,String[] fields,Object[] values){
		this.type = 1;
		this.redisKey = key;
		this.symbolId = symbolId;
		this.field = fields;
		this.value = values;
	}
}
