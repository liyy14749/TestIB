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

    public static List<ContractVO> initContract = new ArrayList<>();
    static {
        initContract.add(new ContractVO("TSLA","STK","USD","ISLAND", 1));
        initContract.add(new ContractVO("AAPL","STK","USD","ISLAND",2));
        initContract.add(new ContractVO("9988","STK","HKD","SEHK",3));
        initContract.add(new ContractVO("IBM","STK","USD","NYSE",4));

    }
}
