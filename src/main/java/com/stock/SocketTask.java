/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.stock;

import cn.hutool.core.date.DateUtil;
import com.ib.client.*;
import com.stock.cache.DataCache;
import com.stock.cache.DataInit;
import com.stock.cache.LastData;
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
	@Autowired private KeyUtil keyUtil;
	@Autowired private DataInit dataInit;

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
				vo.setContract(contract);
				SymbolData symbolData= new SymbolData();
				symbolData.setContract(vo);

				String key = keyUtil.getKeyWithPrefix(String.format("tick_%s_v3",vo.getSymbolId()));
				redisUtil.hashPut(key,"symbol",vo.getSymbol());
				DataCache.symbolCache.put(vo.getSymbolId(), symbolData);
				subscribeTickData(wrapper.getClient(), vo);
				//subscribeMarketDepth(wrapper.getClient(), vo);
				realTimeBars(wrapper.getClient(), vo);
				DataCache.lastDataTime.get(DataCache.klineType).setStartTime(System.currentTimeMillis());
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
						doWork(DataCache.contracts);
					}
				} else {
					LastData lastData = DataCache.lastDataTime.get(DataCache.klineType);
					if(lastData.getStartTime()>0 && (System.currentTimeMillis()-lastData.getStartTime())/1000>10){
						if(DataCache.SERVER_OK && (System.currentTimeMillis()-lastData.getLastTime())/1000>=15){
							if(isTimePeriod()){
								dataInit.reloadRedis();
								log.info("kline no data reconnect");
								DataCache.SERVER_OK = false;
								continue;
							}
						}
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}
	private boolean isTimePeriod(){
		boolean flag = false;
		int hour = DateUtil.thisHour(true);
		int minute = DateUtil.thisMinute();
		if(hour==9){
			if(minute>=29 && minute<=31){
				flag = true;
			}
		} else if((hour==13&&minute==0)||(hour==12&&minute==59)||(hour==13&&minute==1)){
			flag = true;
		} else if(hour==21){
			if(minute>=29 && minute<=31){
				flag = true;
			}
		} else if(hour==22){
			if(minute>=29 && minute<=31){
				flag = true;
			}
		}
		return flag;
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
	private void subscribeTickData(EClientSocket client,ContractVO vo) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(vo,1));
		DataCache.symbolCache.get(vo.getSymbolId()).setMktData(new MktData());
		client.reqMktData(tid, vo.getContract(), "233", false, false, null);
	}

	/**
	 * 订阅市场深度
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private void subscribeMarketDepth(EClientSocket client,ContractVO vo) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(vo,2));
		DataCache.symbolCache.get(vo.getSymbolId()).setMktDepth(new MktDepth());
		client.reqMktDepth(tid, vo.getContract(), 20, false, null);
	}

	public void realTimeBars(EClientSocket client,ContractVO vo) throws InterruptedException {
		int tid = ++tickerId;
		DataCache.tickerCache.put(tid,new TickerVO(vo,3));
		DataCache.symbolCache.get(vo.getSymbolId()).setKLineData(new KLineData());
		client.reqRealTimeBars(tid, vo.getContract(), 5, "TRADES", true, null);
	}

}
