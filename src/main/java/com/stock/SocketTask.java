/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.stock;

import com.ib.client.*;
import com.stock.cache.DataCache;
import com.stock.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketTask {

	private static Logger log = LoggerFactory.getLogger(SocketTask.class);

    static int tickerId;

	public static void start(EWrapperImpl wrapper){

		final EClientSocket m_client = wrapper.getClient();
		final EReaderSignal m_signal = wrapper.getSignal();
		m_client.eConnect("127.0.0.1", 7496, 999);
		final EReader reader = new EReader(m_client, m_signal);

		reader.start();
		//An additional thread is created in this program design to empty the messaging queue
		new Thread(() -> {
			while (m_client.isConnected()) {
				m_signal.waitForSignal();
				try {
					reader.processMsgs();
				} catch (Exception e) {
					log.error("Exception: ",e);
				}
			}
		}).start();
		//! [ereader]
		try {
			Thread.sleep(1000);
			for(ContractVO vo: DataCache.initContract){
				Contract contract = new Contract();
				contract.symbol(vo.getSymbol());
				contract.secType(vo.getSecType());
				contract.currency(vo.getCurrency());
				contract.exchange(vo.getExchange());
				String key = vo.getSymbol()+"_"+vo.getSecType();
				DataCache.symbolCache.put(key, new SymbolData());
				subscribeTickData(wrapper.getClient(), contract, vo, key);
				subscribeMarketDepth(wrapper.getClient(), contract, vo, key);
				realTimeBars(wrapper.getClient(), contract, vo, key);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 订阅市场数据
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private static void subscribeTickData(EClientSocket client,Contract contract,ContractVO vo,String key) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(key,vo));
		DataCache.symbolCache.get(key).setMktData(new MktData());
		client.reqMktData(tid, contract, "", false, false, null);
	}

	/**
	 * 订阅市场深度
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private static void subscribeMarketDepth(EClientSocket client,Contract contract,ContractVO vo,String key) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(key,vo));
		DataCache.symbolCache.get(key).setMktDepth(new MktDepth());
		client.reqMktDepth(tid, contract, 20, false, null);
	}

	private static void realTimeBars(EClientSocket client,Contract contract,ContractVO vo,String key) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(key,vo));
		DataCache.symbolCache.get(key).setKLineData(new KLineData());
		client.reqRealTimeBars(tid, contract, 5, "MIDPOINT", true, null);
	}
}
