package com.stock.vo;

import lombok.Data;

@Data
public class MktData {
    private double bid;//买价格
    private double ask;//卖价格
    private double last;//最新价格
    private double high=0.0;//今日最高
    private double low=0.0;//今日最低
    private double close;//最后交易价格
    private double open=0.0;//开盘价格
    private double price_change;//今日涨跌
    private double price_change_percent;//今天涨跌百分比
}
