/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.stock;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.ib.client.*;
import com.stock.cache.DataCache;
import com.stock.core.util.RedisUtil;
import com.stock.thread.DealMsgThread;
import com.stock.thread.ZSetTask;
import com.stock.thread.TickField4Task;
import com.stock.thread.TickTask;
import com.stock.utils.CommonUtil;
import com.stock.utils.KeyUtil;
import com.stock.vo.ContractVO;
import com.stock.vo.MktData;
import com.stock.vo.SymbolData;
import com.stock.vo.TickerVO;
import com.stock.vo.redisvo.KLineDataRedis;
import com.stock.vo.redisvo.LatestDealRedis;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Service
public class EWrapperImpl implements EWrapper {

    private static Logger log = LoggerFactory.getLogger(EWrapperImpl.class);

    @Autowired private RedisTemplate<String, String> template;
    @Autowired private RedisUtil redisUtil;
    @Autowired
    private KeyUtil keyUtil;
    @Value("${spring.profiles.active}")
    private String env;
    @Autowired private DealMsgThread dealMsgThread;

    private EReaderSignal readerSignal;
    private EClientSocket clientSocket;
    protected int currentOrderId = -1;

    public EWrapperImpl() {
        readerSignal = new EJavaSignal();
        clientSocket = new EClientSocket(this, readerSignal);
    }

    public EClientSocket getClient() {
        return clientSocket;
    }

    public EReaderSignal getSignal() {
        return readerSignal;
    }

    public int getCurrentOrderId() {
        return currentOrderId;
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        log.debug("tick Price. Ticker Id:" + tickerId + ", Field: " + field + ", Price: " + price + ", CanAutoExecute: " + attribs.canAutoExecute()
                + ", pastLimit: " + attribs.pastLimit() + ", pre-open: " + attribs.preOpen());
        TickerVO ticker = DataCache.tickerCache.get(tickerId);
        if (ticker == null) {
            return;
        }
        SymbolData sd = DataCache.symbolCache.get(ticker.getContract().getSymbolId());
        if (sd == null || sd.getMktData() == null) {
            return;
        }
        if(price == -1){
            return;
        }
        ContractVO contractVO = sd.getContract();
        Integer symbolId = contractVO.getSymbolId();
        String key = keyUtil.getKeyWithPrefix(String.format("tick_%s_v3", symbolId));

        // 在合规的时间，才更新redis
        if(!CommonUtil.isValidTime(contractVO)){
            return;
        }

        if (field == 1) {
            if(contractVO.getSecType().equals("IND")){
                return;
            }
            dealMsgThread.putTask(new TickTask(key, symbolId,"bid", price));
        } else if (field == 2) {
            if(contractVO.getSecType().equals("IND")){
                return;
            }
            dealMsgThread.putTask(new TickTask(key, symbolId,"ask", price));
        } else if (field == 4) {
            dealMsgThread.putTask(new TickField4Task(key, symbolId, price));
        } else if (field == 6) {
            dealMsgThread.putTask(new TickTask(key, symbolId,"high", price));
        } else if (field == 7) {
            dealMsgThread.putTask(new TickTask(key, symbolId,"low", price));
        } else if (field == 9) {
            dealMsgThread.putTask(new TickTask(key, symbolId,new String[]{"close","close_at"},
                    new Object[]{price,DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")}));
        } else {
            return;
        }
        dealMsgThread.putTask(new TickTask(key, symbolId,new String[]{"time","date"},
                new Object[]{System.currentTimeMillis()/1000,DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")}));
    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
        log.debug("tick string. Ticker Id:" + tickerId + ", Type: " + tickType + ", Value: " + value);
        TickerVO ticker = DataCache.tickerCache.get(tickerId);
        if (ticker == null) {
            return;
        }
        ContractVO contractVO = ticker.getContract();
        Integer symbolId = contractVO.getSymbolId();
        SymbolData sd = DataCache.symbolCache.get(symbolId);
        MktData mktData = sd.getMktData();
        if (sd == null || mktData == null) {
            return;
        }
        // 在合规的时间，才更新redis
        if(!CommonUtil.isValidTime(contractVO)){
            return;
        }

        if (tickType == 48 && StringUtils.isNotBlank(value)) {
            String[] ss = value.split(";");
            if(!StringUtils.isBlank(ss[0])){
                long time = System.currentTimeMillis()/1000;
                LatestDealRedis deal = new LatestDealRedis();
                deal.setPrice(Double.parseDouble(ss[0]));
                deal.setVolume(Integer.parseInt(ss[1]));
                deal.setTime(time);
                deal.setDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                deal.setMaker(Boolean.parseBoolean(ss[5]));
                String key = keyUtil.getKeyWithPrefix(String.format("order_%s",symbolId));
//                template.opsForZSet().add(key, JSON.toJSONString(deal), time);
                dealMsgThread.putTask(new ZSetTask(key, symbolId, deal, time));
            }
        }
    }

    @Override
    public void tickSize(int tickerId, int field, int size) {
        log.debug("tick Size. Ticker Id:" + tickerId + ", Field: " + field + ", Size: " + size);
        TickerVO ticker = DataCache.tickerCache.get(tickerId);
        if (ticker == null) {
            return;
        }
        SymbolData sd = DataCache.symbolCache.get(ticker.getContract().getSymbolId());
        if (sd == null || sd.getMktData() == null) {
            return;
        }
        if(size == -1){
            return;
        }
        ContractVO contractVO = sd.getContract();
        Integer symbolId = contractVO.getSymbolId();
        String key = keyUtil.getKeyWithPrefix(String.format("tick_%s_v3", symbolId));
        if(contractVO.getSecType().equals("IND")){
            return;
        }
        // 在合规的时间，才更新redis
        if(!CommonUtil.isValidTime(contractVO)){
            return;
        }

        if (field == 0){
            dealMsgThread.putTask(new TickTask(key, symbolId,"ask_size", size));
        } else if (field == 3) {
            dealMsgThread.putTask(new TickTask(key, symbolId,"bid_size", size));
        } else {
            return;
        }
        dealMsgThread.putTask(new TickTask(key, symbolId,new String[]{"time","date"},
                new Object[]{System.currentTimeMillis()/1000,DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")}));
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation,
                               int side, double price, int size) {
        log.debug("depth. " + tickerId + " - Position: " + position + ", Operation: " + operation + ", Side: " + side + ", Price: " + price + ", Size: " + size + "");

        TickerVO ticker = DataCache.tickerCache.get(tickerId);
        if (ticker == null) {
            return;
        }
        SymbolData sd = DataCache.symbolCache.get(ticker.getContract().getSymbolId());
        if (sd == null || sd.getMktDepth() == null) {
            return;
        }
        if (operation == 0 || operation == 1) {
            if (side == 0) {
                sd.getMktDepth().getAsk().put(position, new Object[]{price, size});
            } else if (side == 1) {
                sd.getMktDepth().getBid().put(position, new Object[]{price, size});
            }
        } else if (operation == 2) {
            if (side == 0) {
                sd.getMktDepth().getAsk().remove(position);
            } else if (side == 1) {
                sd.getMktDepth().getBid().remove(position);
            }
        }
        sd.getMktDepth().setTime(System.currentTimeMillis());
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position,
                                 String marketMaker, int operation, int side, double price, int size, boolean isSmartDepth) {
        log.debug("depth. " + tickerId + " - Position: " + position + ", Operation: " + operation + ", Side: " + side + ", Price: " + price + ", Size: " + size + ", isSmartDepth: " + isSmartDepth);
        TickerVO ticker = DataCache.tickerCache.get(tickerId);
        if (ticker == null) {
            return;
        }
        SymbolData sd = DataCache.symbolCache.get(ticker.getContract().getSymbolId());
        if (sd == null || sd.getMktDepth() == null) {
            return;
        }
        if (operation == 0 || operation == 1) {
            if (side == 0) {
                sd.getMktDepth().getAsk().put(position, new Object[]{price, size});
            } else if (side == 1) {
                sd.getMktDepth().getBid().put(position, new Object[]{price, size});
            }
        } else if (operation == 2) {
            if (side == 0) {
                sd.getMktDepth().getAsk().remove(position);
            } else if (side == 1) {
                sd.getMktDepth().getBid().remove(position);
            }
        }
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high,
                            double low, double close, long volume, double wap, int count) {
        log.debug("kline. " + reqId + " - Time: " + time + ", Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close + ", Volume: " + volume + ", Count: " + count + ", WAP: " + wap);
        DataCache.lastDataTime.get(DataCache.klineType).setLastTime(System.currentTimeMillis());
        TickerVO ticker = DataCache.tickerCache.get(reqId);
        if (ticker == null) {
            return;
        }
        Integer symbolId = ticker.getContract().getSymbolId();
        SymbolData sd = DataCache.symbolCache.get(symbolId);
        if(sd == null){
            return;
        }
        KLineDataRedis rd = new KLineDataRedis();
        rd.setOpen(open);
        rd.setClose(close);
        rd.setHigh(high);
        rd.setLow(low);
        rd.setVolume(volume);
        rd.setTime(time);
        rd.setDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        rd.setSymbol(ticker.getContract().getSymbol());
        String sb = keyUtil.getKeyWithPrefix(String.format("kline_%s_5sec", symbolId));
        dealMsgThread.putTask(new ZSetTask(sb, symbolId,rd, time));

        if(env.equals("dev")){
            String key = keyUtil.getKeyWithPrefix(String.format("tick_%s_v3",sd.getContract().getSymbolId()));
            //redisUtil.hashPut(key,"last_close",open);
            dealMsgThread.putTask(new TickTask(key, symbolId,"last_close", open));
            //redisUtil.hashPut(key,"open",open);
            dealMsgThread.putTask(new TickTask(key, symbolId,"open", open));
        }
    }

    @Override
    public void tickOptionComputation(int tickerId, int field,
                                      double impliedVol, double delta, double optPrice,
                                      double pvDividend, double gamma, double vega, double theta,
                                      double undPrice) {
		log.debug("TickOptionComputation. TickerId: "+tickerId+", field: "+field+", ImpliedVolatility: "+impliedVol+", Delta: "+delta
                +", OptionPrice: "+optPrice+", pvDividend: "+pvDividend+", Gamma: "+gamma+", Vega: "+vega+", Theta: "+theta+", UnderlyingPrice: "+undPrice);
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        log.debug("Tick Generic. Ticker Id:" + tickerId + ", Field: " + TickType.getField(tickType) + ", Value: " + value);
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints,
                        String formattedBasisPoints, double impliedFuture, int holdDays,
                        String futureLastTradeDate, double dividendImpact,
                        double dividendsToLastTradeDate) {
		log.debug("TickEFP. "+tickerId+", Type: "+tickType+", BasisPoints: "+basisPoints+", FormattedBasisPoints: "+
			formattedBasisPoints+", ImpliedFuture: "+impliedFuture+", HoldDays: "+holdDays+", FutureLastTradeDate: "+futureLastTradeDate+
			", DividendImpact: "+dividendImpact+", DividendsToLastTradeDate: "+dividendsToLastTradeDate);
    }

    @Override
    public void orderStatus(int orderId, String status, double filled,
                            double remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		log.debug("OrderStatus. Id: "+orderId+", Status: "+status+", Filled"+filled+", Remaining: "+remaining
                +", AvgFillPrice: "+avgFillPrice+", PermId: "+permId+", ParentId: "+parentId+", LastFillPrice: "+lastFillPrice+
                ", ClientId: "+clientId+", WhyHeld: "+whyHeld+", MktCapPrice: "+mktCapPrice);
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order,
                          OrderState orderState) {
		log.debug(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
    }

    @Override
    public void openOrderEnd() {
		log.debug("OpenOrderEnd");
    }

    @Override
    public void updateAccountValue(String key, String value, String currency,
                                   String accountName) {
		log.debug("UpdateAccountValue. Key: " + key + ", Value: " + value + ", Currency: " + currency + ", AccountName: " + accountName);
    }

    @Override
    public void updatePortfolio(Contract contract, double position,
                                double marketPrice, double marketValue, double averageCost,
                                double unrealizedPNL, double realizedPNL, String accountName) {
		log.debug("UpdatePortfolio. "+contract.symbol()+", "+contract.secType()+" @ "+contract.exchange()
                +": Position: "+position+", MarketPrice: "+marketPrice+", MarketValue: "+marketValue+", AverageCost: "+averageCost
                +", UnrealizedPNL: "+unrealizedPNL+", RealizedPNL: "+realizedPNL+", AccountName: "+accountName);
    }

    @Override
    public void updateAccountTime(String timeStamp) {
		log.debug("UpdateAccountTime. Time: " + timeStamp+"\n");
    }

    @Override
    public void accountDownloadEnd(String accountName) {
		log.debug("Account download finished: "+accountName+"\n");
    }

    @Override
    public void nextValidId(int orderId) {
		log.debug("Next Valid Id: ["+orderId+"]");
		currentOrderId = orderId;
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
		log.debug(EWrapperMsgGenerator.contractDetails(reqId, contractDetails));
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		log.debug(EWrapperMsgGenerator.bondContractDetails(reqId, contractDetails));
    }

    @Override
    public void contractDetailsEnd(int reqId) {
		log.debug("ContractDetailsEnd. "+reqId+"\n");
    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
		log.debug("ExecDetails. "+reqId+" - ["+contract.symbol()+"], ["+contract.secType()+"], ["+contract.currency()+"], ["+execution.execId()+
		        "], ["+execution.orderId()+"], ["+execution.shares()+"]"  + ", [" + execution.lastLiquidity() + "]");
    }

    @Override
    public void execDetailsEnd(int reqId) {
		log.debug("ExecDetailsEnd. "+reqId+"\n");
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message,
                                   String origExchange) {
		log.debug("News Bulletins. "+msgId+" - Type: "+msgType+", Message: "+message+", Exchange of Origin: "+origExchange+"\n");
    }

    @Override
    public void managedAccounts(String accountsList) {
		log.debug("Account list: " +accountsList);
    }

    @Override
    public void receiveFA(int faDataType, String xml) {
		log.debug("Receiving FA: "+faDataType+" - "+xml);
    }

    @Override
    public void historicalData(int reqId, Bar bar) {
		log.debug("HistoricalData. "+reqId+" - Date: "+bar.time()+", Open: "+bar.open()+", High: "+bar.high()+", Low: "+bar.low()+", Close: "+bar.close()+", Volume: "+bar.volume()+", Count: "+bar.count()+", WAP: "+bar.wap());
    }

    @Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
		log.debug("HistoricalDataEnd. "+reqId+" - Start Date: "+startDateStr+", End Date: "+endDateStr);
    }

    @Override
    public void scannerParameters(String xml) {
		log.debug("ScannerParameters. "+xml+"\n");
    }

    @Override
    public void scannerData(int reqId, int rank,
                            ContractDetails contractDetails, String distance, String benchmark,
                            String projection, String legsStr) {
		log.debug("ScannerData. "+reqId+" - Rank: "+rank+", Symbol: "+contractDetails.contract().symbol()+", SecType: "+contractDetails.contract().secType()+", Currency: "+contractDetails.contract().currency()
                +", Distance: "+distance+", Benchmark: "+benchmark+", Projection: "+projection+", Legs String: "+legsStr);
    }

    @Override
    public void scannerDataEnd(int reqId) {
		log.debug("ScannerDataEnd. "+reqId);
    }

    @Override
    public void currentTime(long time) {
		log.debug("currentTime");
    }

    @Override
    public void fundamentalData(int reqId, String data) {
		log.debug("FundamentalData. ReqId: ["+reqId+"] - Data: ["+data+"]");
    }

    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
		log.debug("deltaNeutralValidation");
    }

    @Override
    public void tickSnapshotEnd(int reqId) {
		log.debug("TickSnapshotEnd: "+reqId);
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
		log.debug("MarketDataType. ["+reqId+"], Type: ["+marketDataType+"]\n");
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
		log.debug("CommissionReport. ["+commissionReport.execId()+"] - ["+commissionReport.commission()+"] ["+commissionReport.currency()+"] RPNL ["+commissionReport.realizedPNL()+"]");
    }

    @Override
    public void position(String account, Contract contract, double pos,
                         double avgCost) {
		log.debug("Position. "+account+" - Symbol: "+contract.symbol()+", SecType: "+contract.secType()+", Currency: "+contract.currency()+", Position: "+pos+", Avg cost: "+avgCost);
    }

    @Override
    public void positionEnd() {
		log.debug("PositionEnd \n");
    }

    @Override
    public void accountSummary(int reqId, String account, String tag,
                               String value, String currency) {
		log.debug("Acct Summary. ReqId: " + reqId + ", Acct: " + account + ", Tag: " + tag + ", Value: " + value + ", Currency: " + currency);
    }

    @Override
    public void accountSummaryEnd(int reqId) {
		log.debug("AccountSummaryEnd. Req Id: "+reqId+"\n");
    }

    @Override
    public void verifyMessageAPI(String apiData) {
        log.debug("verifyMessageAPI");
    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
        log.debug("verifyCompleted");
    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {
        log.debug("verifyAndAuthMessageAPI");
    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
        log.debug("verifyAndAuthCompleted");
    }

    @Override
    public void displayGroupList(int reqId, String groups) {
        log.debug("Display Group List. ReqId: " + reqId + ", Groups: " + groups + "\n");
    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
        log.debug("Display Group Updated. ReqId: " + reqId + ", Contract info: " + contractInfo + "\n");
    }

    @Override
    public void error(Exception e) {
        log.error("Exception: ", e);
    }

    @Override
    public void error(String str) {
        log.error("Error STR");
    }

    @Override
    public void error(int id, int errorCode, String errorMsg) {
        log.error("Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg + "\n");
        if(id == -1 && (errorCode ==2104|| errorCode ==2106|| errorCode==2158)){
            DataCache.SERVER_OK = true;
        } else if(id == -1 && (errorCode == 1300 || errorCode == 504 || errorCode == 507 || errorCode == 502 || errorCode == 1101)){
            DataCache.SERVER_OK = false;
            return;
        }
    }

    @Override
    public void connectionClosed() {
        log.debug("Connection closed");
    }

    @Override
    public void connectAck() {
        if (clientSocket.isAsyncEConnect()) {
            log.debug("Acknowledging connection");
            clientSocket.startAPI();
        }
    }

    @Override
    public void positionMulti(int reqId, String account, String modelCode,
                              Contract contract, double pos, double avgCost) {
        log.debug("Position Multi. Request: " + reqId + ", Account: " + account + ", ModelCode: " + modelCode + ", Symbol: " + contract.symbol() + ", SecType: " + contract.secType() + ", Currency: " + contract.currency() + ", Position: " + pos + ", Avg cost: " + avgCost + "\n");
    }

    @Override
    public void positionMultiEnd(int reqId) {
        log.debug("Position Multi End. Request: " + reqId + "\n");
    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode,
                                   String key, String value, String currency) {
        log.debug("Account Update Multi. Request: " + reqId + ", Account: " + account + ", ModelCode: " + modelCode + ", Key: " + key + ", Value: " + value + ", Currency: " + currency + "\n");
    }

    @Override
    public void accountUpdateMultiEnd(int reqId) {
        log.debug("Account Update Multi End. Request: " + reqId + "\n");
    }

    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange,
                                                    int underlyingConId, String tradingClass, String multiplier,
                                                    Set<String> expirations, Set<Double> strikes) {
        log.debug("Security Definition Optional Parameter. Request: " + reqId + ", Trading Class: " + tradingClass + ", Multiplier: " + multiplier + " \n");
    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {
        log.debug("Security Definition Optional Parameter End. Request: " + reqId);
    }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
        for (SoftDollarTier tier : tiers) {
            log.debug("tier: " + tier.toString() + ", ");
        }
    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {
        for (FamilyCode fc : familyCodes) {
            log.debug("Family Code. AccountID: " + fc.accountID() + ", FamilyCode: " + fc.familyCodeStr());
        }
    }

    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        log.debug("Contract Descriptions. Request: " + reqId + "\n");
        for (ContractDescription cd : contractDescriptions) {
            Contract c = cd.contract();
            StringBuilder derivativeSecTypesSB = new StringBuilder();
            for (String str : cd.derivativeSecTypes()) {
                derivativeSecTypesSB.append(str);
                derivativeSecTypesSB.append(",");
            }
            log.debug("Contract. ConId: " + c.conid() + ", Symbol: " + c.symbol() + ", SecType: " + c.secType() +
                    ", PrimaryExch: " + c.primaryExch() + ", Currency: " + c.currency() +
                    ", DerivativeSecTypes:[" + derivativeSecTypesSB.toString() + "]");
        }
    }

    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
        for (DepthMktDataDescription depthMktDataDescription : depthMktDataDescriptions) {
            log.debug("Depth Mkt Data Description. Exchange: " + depthMktDataDescription.exchange() +
                    ", ListingExch: " + depthMktDataDescription.listingExch() +
                    ", SecType: " + depthMktDataDescription.secType() +
                    ", ServiceDataType: " + depthMktDataDescription.serviceDataType() +
                    ", AggGroup: " + depthMktDataDescription.aggGroup()
            );
        }
    }

    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
        log.debug("Tick News. TickerId: " + tickerId + ", TimeStamp: " + timeStamp + ", ProviderCode: " + providerCode + ", ArticleId: " + articleId + ", Headline: " + headline + ", ExtraData: " + extraData + "\n");
    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
        log.debug("smart components req id:" + reqId);

        for (Map.Entry<Integer, Entry<String, Character>> item : theMap.entrySet()) {
            log.debug("bit number: " + item.getKey() +
                    ", exchange: " + item.getValue().getKey() + ", exchange letter: " + item.getValue().getValue());
        }
    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
        log.debug("Tick req params. Ticker Id:" + tickerId + ", Min tick: " + minTick + ", bbo exchange: " + bboExchange + ", Snapshot permissions: " + snapshotPermissions);
    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {
        for (NewsProvider np : newsProviders) {
            log.debug("News Provider. ProviderCode: " + np.providerCode() + ", ProviderName: " + np.providerName() + "\n");
        }

    }

    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {
        log.debug("News Article. Request Id: " + requestId + ", ArticleType: " + articleType +
                ", ArticleText: " + articleText);
    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        log.debug("Historical News. RequestId: " + requestId + ", Time: " + time + ", ProviderCode: " + providerCode + ", ArticleId: " + articleId + ", Headline: " + headline + "\n");
    }

    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {
        log.debug("Historical News End. RequestId: " + requestId + ", HasMore: " + hasMore + "\n");
    }

    @Override
    public void headTimestamp(int reqId, String headTimestamp) {
        log.debug("Head timestamp. Req Id: " + reqId + ", headTimestamp: " + headTimestamp);
    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        log.debug(EWrapperMsgGenerator.histogramData(reqId, items));
    }

    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {
        log.debug("HistoricalDataUpdate. " + reqId + " - Date: " + bar.time() + ", Open: " + bar.open() + ", High: " + bar.high() + ", Low: " + bar.low() + ", Close: " + bar.close() + ", Volume: " + bar.volume() + ", Count: " + bar.count() + ", WAP: " + bar.wap());
    }

    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {
        log.debug(EWrapperMsgGenerator.rerouteMktDataReq(reqId, conId, exchange));
    }

    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
        log.debug(EWrapperMsgGenerator.rerouteMktDepthReq(reqId, conId, exchange));
    }

    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setMaximumFractionDigits(340);
        log.debug("Market Rule Id: " + marketRuleId);
        for (PriceIncrement pi : priceIncrements) {
            log.debug("Price Increment. Low Edge: " + df.format(pi.lowEdge()) + ", Increment: " + df.format(pi.increment()));
        }
    }

    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
        log.debug(EWrapperMsgGenerator.pnl(reqId, dailyPnL, unrealizedPnL, realizedPnL));
    }

    @Override
    public void pnlSingle(int reqId, int pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
        log.debug(EWrapperMsgGenerator.pnlSingle(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value));
    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        for (HistoricalTick tick : ticks) {
            log.debug(EWrapperMsgGenerator.historicalTick(reqId, tick.time(), tick.price(), tick.size()));
        }
    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        for (HistoricalTickBidAsk tick : ticks) {
            log.debug(EWrapperMsgGenerator.historicalTickBidAsk(reqId, tick.time(), tick.tickAttribBidAsk(), tick.priceBid(), tick.priceAsk(), tick.sizeBid(),
                    tick.sizeAsk()));
        }
    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        for (HistoricalTickLast tick : ticks) {
            log.debug(EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.tickAttribLast(), tick.price(), tick.size(), tick.exchange(),
                    tick.specialConditions()));
        }
    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, int size, TickAttribLast tickAttribLast,
                                  String exchange, String specialConditions) {
        log.debug(EWrapperMsgGenerator.tickByTickAllLast(reqId, tickType, time, price, size, tickAttribLast, exchange, specialConditions));
    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize,
                                 TickAttribBidAsk tickAttribBidAsk) {
        log.debug(EWrapperMsgGenerator.tickByTickBidAsk(reqId, time, bidPrice, askPrice, bidSize, askSize, tickAttribBidAsk));
    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        log.debug(EWrapperMsgGenerator.tickByTickMidPoint(reqId, time, midPoint));
    }

    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        log.debug(EWrapperMsgGenerator.orderBound(orderId, apiClientId, apiOrderId));
    }

    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        log.debug(EWrapperMsgGenerator.completedOrder(contract, order, orderState));
    }

    @Override
    public void completedOrdersEnd() {
        log.debug(EWrapperMsgGenerator.completedOrdersEnd());
    }
}
