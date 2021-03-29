package com.stock.cache;

import com.stock.vo.ContractVO;
import com.stock.vo.SymbolData;
import com.stock.vo.TickerVO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataCache {
    public static Map<Integer, SymbolData> symbolCache = Collections.synchronizedMap(new LinkedHashMap<>());
    public static Map<Integer, TickerVO> tickerCache = new ConcurrentHashMap<>();
    public static boolean SERVER_OK = false;
    public static Map<String, LastData> lastDataTime = new ConcurrentHashMap<>();
    public static String klineType = "kline";
    public static String tickType = "tick";

    public static List<ContractVO> usContracts;
    public static List<ContractVO> hkContracts;
    public static List<ContractVO> indContracts;

    static {
        lastDataTime.put(klineType,new LastData());
//        lastDataTime.put(tickType,new LastData());
    }
}
