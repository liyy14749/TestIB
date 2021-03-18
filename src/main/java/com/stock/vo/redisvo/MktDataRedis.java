package com.stock.vo.redisvo;

import lombok.Data;

@Data
public class MktDataRedis {
    private String symbol;
    private long time;
    private String date;
    private double bid;//买价格
    private double ask;//卖价格
    private double last;//最新价格
    private double high;//今日最高
    private double low;//今日最低
    private double close;//最后交易价格
    private double open;//开盘价格
    private double price_change;//今日涨跌
    private double price_change_percent;//今天涨跌百分比
}
