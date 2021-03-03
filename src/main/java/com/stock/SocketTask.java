/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.stock;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ib.client.*;
import com.stock.cache.DataMap;
import com.stock.contracts.ContractSamples;
import com.stock.vo.ContractVO;
import com.stock.vo.TickerVO;
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
		// A pause to give the application time to establish the connection
		// In a production application, it would be best to wait for callbacks to confirm the connection is complete
		try {
			Thread.sleep(1000);
//			subscribeTickData(wrapper.getClient());
			subscribeMarketDepth(wrapper.getClient());
//			subscribeHistoricalData(wrapper.getClient());
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
	private static void subscribeTickData(EClientSocket client) throws InterruptedException {
		/*** Requesting real time market data ***/
		//Thread.sleep(1000);
		//! [reqmktdata]
		for(ContractVO vo:DataMap.initContract){
			Contract contract = new Contract();
			contract.symbol(vo.getSymbol());
			contract.secType(vo.getSecType());
			contract.currency(vo.getCurrency());
			contract.exchange(vo.getExchange());
			contract.strike(0);
			contract.includeExpired(false);
			int tid = ++tickerId;
			DataMap.tickerCache.put(tid,new TickerVO(tid,vo.getSymbol()));
			client.reqMktData(tid, contract, "", false, false, null);
		}
		//! [reqmktdata]

		//! [reqsmartcomponents]
		//client.reqSmartComponents(1013, "a6");
		//! [reqsmartcomponents]

	}

	/**
	 * 订阅历史数据
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private static void subscribeHistoricalData(EClientSocket client) throws InterruptedException {
		
		/*** Requesting historical data ***/

		//! [reqHeadTimeStamp]
		client.reqHeadTimestamp(4003, ContractSamples.USStock(), "TRADES", 1, 1);
		//! [reqHeadTimeStamp]

		//! [cancelHeadTimestamp]
		client.cancelHeadTimestamp(4003);
		//! [cancelHeadTimestamp]
		
		//! [reqhistoricaldata]
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String formatted = form.format(cal.getTime());
		client.reqHistoricalData(4001, ContractSamples.EurGbpFx(), formatted, "1 M", "1 day", "MIDPOINT", 1, 1, false, null);
		client.reqHistoricalData(4002, ContractSamples.EuropeanStock(), formatted, "10 D", "1 min", "TRADES", 1, 1, false, null);
		Thread.sleep(2000);
		/*** Canceling historical data requests ***/
		client.cancelHistoricalData(4001);
        client.cancelHistoricalData(4002);
		//! [reqhistoricaldata]
		return;
		//! [reqHistogramData]
		/*client.reqHistogramData(4004, ContractSamples.USStock(), false, "3 days");
        //! [reqHistogramData]
		Thread.sleep(5);
		
		//! [cancelHistogramData]
        client.cancelHistogramData(4004);*/
		//! [cancelHistogramData]
	}

	/**
	 * 订阅市场深度
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private static void subscribeMarketDepth(EClientSocket client) throws InterruptedException {

		/*** Requesting the Deep Book ***/

		//! [reqMktDepthExchanges]
//		client.reqMktDepthExchanges();
		//! [reqMktDepthExchanges]
		for(ContractVO vo:DataMap.initContract){
			Contract contract = new Contract();
			contract.symbol(vo.getSymbol());
			contract.secType(vo.getSecType());
			contract.currency(vo.getCurrency());
			contract.exchange(vo.getExchange());
			contract.strike(0);
			contract.includeExpired(false);
			int tid = ++tickerId;
			DataMap.tickerCache.put(tid,new TickerVO(tid,vo.getSymbol()));
			client.reqMktDepth(tid, contract, 5, false, null);
		}
		//! [reqmarketdepth]

	}
}
