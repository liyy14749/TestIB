/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.stock;

import com.ib.client.*;
import com.stock.cache.DataCache;
import com.stock.core.util.RedisUtil;
import com.stock.utils.KeyUtil;
import com.stock.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SocketTask {
	@Autowired EWrapperImpl wrapper;
	@Autowired private RedisUtil redisUtil;
	@Value("${my.ib.server.host}")
	private String ip;
	@Value("${my.ib.server.port}")
	private Integer port;
	@Value("${my.ib.server.clientId}")
	private Integer clientId;
	@Autowired
	private KeyUtil keyUtil;

	private static Logger log = LoggerFactory.getLogger(SocketTask.class);

    static int tickerId;

	public void start(){
		final EClientSocket m_client = wrapper.getClient();
		final EReaderSignal m_signal = wrapper.getSignal();
		reconnectThreadRun(m_client, m_signal);
	}

	private void doWork(List<ContractVO> contracts){
		try {
			Thread.sleep(1000);
			for(ContractVO vo: contracts){
				Contract contract = new Contract();
				contract.symbol(vo.getSymbol());
				contract.secType(vo.getSecType());
				contract.currency(vo.getCurrency());
				contract.exchange(vo.getExchange());
				SymbolData symbolData= new SymbolData();
				symbolData.setContract(vo);

				String key = keyUtil.getKeyWithPrefix(String.format("tick_%s_v3",vo.getSymbolId()));
				redisUtil.hashPut(key,"symbol",vo.getSymbol());
				DataCache.symbolCache.put(vo.getSymbolId(), symbolData);
				subscribeTickData(wrapper.getClient(), contract, vo);
				subscribeMarketDepth(wrapper.getClient(), contract, vo);
				realTimeBars(wrapper.getClient(), contract, vo);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void reconnectThreadRun(final EClientSocket m_client, final EReaderSignal m_signal){
		new Thread(() -> {
			while (true) {
				if (!DataCache.SERVER_OK) {
					log.info("reconnect");
					reconnect(m_client, m_signal);
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
					}
					if(DataCache.SERVER_OK){
						doWork(DataCache.usContracts);
						doWork(DataCache.hkContracts);
						doWork(DataCache.indContracts);
					}
				} else {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}
	private void reconnect(EClientSocket m_client, EReaderSignal m_signal) {
		m_client.eDisconnect();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
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
		}
	}
	/**
	 * 订阅市场数据
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private void subscribeTickData(EClientSocket client,Contract contract,ContractVO vo) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(vo));
		DataCache.symbolCache.get(vo.getSymbolId()).setMktData(new MktData());
		client.reqMktData(tid, contract, "233", false, false, null);
	}

	/**
	 * 订阅市场深度
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private void subscribeMarketDepth(EClientSocket client,Contract contract,ContractVO vo) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(vo));
		DataCache.symbolCache.get(vo.getSymbolId()).setMktDepth(new MktDepth());
		client.reqMktDepth(tid, contract, 20, false, null);
	}

	private void realTimeBars(EClientSocket client,Contract contract,ContractVO vo) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(vo));
		DataCache.symbolCache.get(vo.getSymbolId()).setKLineData(new KLineData());
		client.reqRealTimeBars(tid, contract, 5, "TRADES", true, null);
	}
}
