package com.stock.cache;

import com.stock.vo.ContractVO;
import com.stock.vo.MktData;
import com.stock.vo.TickerVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataMap {
    public static Map<Integer, MktData> cache = new ConcurrentHashMap<>();
    public static Map<Integer, TickerVO> tickerCache = new ConcurrentHashMap<>();

    public static List<ContractVO> initContract = new ArrayList<>();
    static {
        initContract.add(new ContractVO("EUR","CASH","USD","IDEALPRO"));
        initContract.add(new ContractVO("GBP","CASH","USD","IDEALPRO"));
        initContract.add(new ContractVO("USD","CASH","JPY","IDEALPRO"));
    }
}
