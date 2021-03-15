package com.stock.cache;

import com.stock.vo.ContractVO;
import com.stock.vo.SymbolData;
import com.stock.vo.TickerVO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class DataCache {
    public static Map<Integer, SymbolData> symbolCache = Collections.synchronizedMap(new LinkedHashMap<>());
    public static Map<Integer, TickerVO> tickerCache = new ConcurrentHashMap<>();
    public static boolean SERVER_OK = false;

    public static List<ContractVO> usContracts;
    public static List<ContractVO> hkContracts;

    public static Semaphore semaphore = new Semaphore(3);
//    static {
//        usContracts.add(new ContractVO("TSLA","STK","USD","ISLAND", 1));
//        usContracts.add(new ContractVO("AAPL","STK","USD","ISLAND",2));
//        usContracts.add(new ContractVO("9988","STK","HKD","SEHK",3));
//        usContracts.add(new ContractVO("IBM","STK","USD","ISLAND",4));
//        usContracts.add(new ContractVO("939","STK","HKD","SEHK",5));
//        usContracts.add(new ContractVO("1810","STK","HKD","SEHK",6));
//
//    }
}
