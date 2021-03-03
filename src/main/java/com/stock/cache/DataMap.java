package com.stock.cache;

import com.stock.vo.MktData;
import com.stock.vo.TickerVO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataMap {
    public static Map<Integer, MktData> cache = new ConcurrentHashMap<>();
    public static Map<Integer, TickerVO> tickerCache = new ConcurrentHashMap<>();

}
