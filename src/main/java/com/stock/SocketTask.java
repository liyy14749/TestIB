/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.stock;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ib.client.*;
import com.stock.cache.DataMap;
import com.stock.contracts.ContractSamples;
import com.stock.vo.MktData;

public class SocketTask {

	public static void main(String[] args) throws InterruptedException {
		EWrapperImpl wrapper = new EWrapperImpl();
		SocketTask.start(wrapper);
	}

	public static void start(EWrapperImpl wrapper){

		final EClientSocket m_client = wrapper.getClient();
		final EReaderSignal m_signal = wrapper.getSignal();
		//! [connect]
		m_client.eConnect("127.0.0.1", 7496, 999);
		//! [connect]
		//! [ereader]
		final EReader reader = new EReader(m_client, m_signal);

		reader.start();
		//An additional thread is created in this program design to empty the messaging queue
		new Thread(() -> {
			while (m_client.isConnected()) {
				m_signal.waitForSignal();
				try {
					reader.processMsgs();
				} catch (Exception e) {
					System.out.println("Exception: "+e.getMessage());
				}
			}
		}).start();
		//! [ereader]
		// A pause to give the application time to establish the connection
		// In a production application, it would be best to wait for callbacks to confirm the connection is complete
		try {
			Thread.sleep(1000);
			tickDataOperationsTest(wrapper.getClient());
	//		marketDepthOperationsTest(wrapper.getClient());
	//		historicalDataRequests(wrapper.getClient());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 市场数据
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private static void tickDataOperationsTest(EClientSocket client) throws InterruptedException {

		Contract contract = new Contract();
		contract.symbol("EUR");
		contract.secType("CASH");
		contract.currency("USD");
		contract.exchange("IDEALPRO");
		contract.strike(0);
		contract.includeExpired(false);

		/*** Requesting real time market data ***/
		//Thread.sleep(1000);
		//! [reqmktdata]
		int tickerId = 1001;
		DataMap.cache.put(tickerId,new MktData(tickerId));
		client.reqMktData(tickerId, contract, "", false, false, null);
		//! [reqmktdata]

		//! [reqsmartcomponents]
		//client.reqSmartComponents(1013, "a6");
		//! [reqsmartcomponents]

	}

	/**
	 * 历史数据
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private static void historicalDataRequests(EClientSocket client) throws InterruptedException {
		
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
	 * 市场深度
	 *
	 * @param client
	 * @throws InterruptedException
	 */
	private static void marketDepthOperationsTest(EClientSocket client) throws InterruptedException {

		/*** Requesting the Deep Book ***/

		//! [reqMktDepthExchanges]
		client.reqMktDepthExchanges();
		//! [reqMktDepthExchanges]

		//! [reqmarketdepth]
		client.reqMktDepth(2001, ContractSamples.EurGbpFx(), 5, false, null);

	}
}
