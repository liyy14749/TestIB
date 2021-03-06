package com.stock.vo;

import lombok.Data;

@Data
public class KLineData {
    private Long T;
    private double o;//open柱开始价格
    private double c;//close柱结束价格
    private double h;//high柱最高价格
    private double l;//柱最低价格
    private long v;//成交量
}
