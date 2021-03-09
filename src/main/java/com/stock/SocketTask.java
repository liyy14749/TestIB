/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.stock;

import com.ib.client.*;
import com.stock.cache.DataCache;
import com.stock.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SocketTask {
	@Autowired EWrapperImpl wrapper;

	@Value("${my.ib.server.host}")
	private String ip;
	@Value("${my.ib.server.port}")
	private Integer port;
	@Value("${my.ib.server.clientId}")
	private Integer clientId;

	private static Logger log = LoggerFactory.getLogger(SocketTask.class);

    static int tickerId;

	public void start(){
		final EClientSocket m_client = wrapper.getClient();
		final EReaderSignal m_signal = wrapper.getSignal();
		m_client.eConnect(ip, port, clientId);
		reconnect(m_client, m_signal);
		reconnectThreadRun(m_client, m_signal);
	}

	private void doWork(){
		try {
			Thread.sleep(1000);
			for(ContractVO vo: DataCache.initContract){
				Contract contract = new Contract();
				contract.symbol(vo.getSymbol());
				contract.secType(vo.getSecType());
				contract.currency(vo.getCurrency());
				contract.exchange(vo.getExchange());
				String key = vo.getSymbol()+"_"+vo.getSecType()+"_"+vo.getCurrency();
				SymbolData symbolData= new SymbolData();
				symbolData.setContract(vo);
				DataCache.symbolCache.put(key, symbolData);
				subscribeTickData(wrapper.getClient(), contract, vo, key);
				subscribeMarketDepth(wrapper.getClient(), contract, vo, key);
				realTimeBars(wrapper.getClient(), contract, vo, key);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void reconnectThreadRun(final EClientSocket m_client, final EReaderSignal m_signal){
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				if (!DataCache.SERVER_OK) {
					log.info("reconnect");
					reconnect(m_client, m_signal);
				}
			}
		}).start();
	}
	private void reconnect(EClientSocket m_client, EReaderSignal m_signal) {
		m_client.eConnect(ip, port, clientId);
		final EReader reader = new EReader(m_client, m_signal);
		if (m_client.isConnected()) {
			reader.start();
			new Thread(() -> {
				while (m_client.isConnected()) {
					m_signal.waitForSignal();
					try {
						reader.processMsgs();
					} catch (Exception e) {
						log.error("Exception: ", e);
					}
				}
			}).start();
			doWork();
		}
	}
	/**
	 * 订阅市场数据
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private void subscribeTickData(EClientSocket client,Contract contract,ContractVO vo,String key) throws InterruptedException {
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
	private void subscribeMarketDepth(EClientSocket client,Contract contract,ContractVO vo,String key) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(key,vo));
		DataCache.symbolCache.get(key).setMktDepth(new MktDepth());
		client.reqMktDepth(tid, contract, 20, false, null);
	}

	private void realTimeBars(EClientSocket client,Contract contract,ContractVO vo,String key) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(key,vo));
		DataCache.symbolCache.get(key).setKLineData(new KLineData());
		client.reqRealTimeBars(tid, contract, 5, "MIDPOINT", true, null);
	}
}
