package com.stock.vo;

import lombok.Data;

@Data
public class KLineData {
    private double open;//open柱开始价格
    private double close;//close柱结束价格
    private double high=0.0;//high柱最高价格
    private double low=0.0;//柱最低价格
    private long volume;//成交量
}
