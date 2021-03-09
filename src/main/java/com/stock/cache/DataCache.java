package com.stock.cache;

import com.stock.vo.ContractVO;
import com.stock.vo.SymbolData;
import com.stock.vo.TickerVO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataCache {
    public static Map<String, SymbolData> symbolCache = Collections.synchronizedMap(new LinkedHashMap<>());
    public static Map<Integer, TickerVO> tickerCache = new ConcurrentHashMap<>();
    public static boolean SERVER_OK = false;

    public static List<ContractVO> initContract = new ArrayList<>();
    static {
        initContract.add(new ContractVO("EUR","CASH","USD","IDEALPRO"));
        initContract.add(new ContractVO("GBP","CASH","USD","IDEALPRO"));
        initContract.add(new ContractVO("USD","CASH","JPY","IDEALPRO"));
    }
}
