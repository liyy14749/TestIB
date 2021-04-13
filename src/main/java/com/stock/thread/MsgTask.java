package com.stock.thread;

import lombok.Data;

@Data
public class MsgTask {
	// 1-tick，2-kline
	protected Integer type;
	protected String redisKey;
	protected Integer symbolId;
}
