package com.stock.vo.redisvo;

import lombok.Data;

@Data
public class KLineDataRedis {
    private String symbol;
    private Long time;
    private double open;//open柱开始价格
    private double close;//close柱结束价格
    private double high;//high柱最高价格
    private double low;//柱最低价格
    private long volume;//成交量
}
