package com.stock.core.util;

import java.util.concurrent.ExecutorService;

import cn.hutool.core.thread.ThreadUtil;

public class ThreadPool {

	private static ExecutorService executorService = null;
	
	static {
		executorService = ThreadUtil.newExecutor(20);
	}

	public static ExecutorService getExecutorService() {
		return executorService;
	}
	
}
