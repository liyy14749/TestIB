package com.stock.vo;

import lombok.Data;

@Data
public class MktData {
    private double b;//买价格
    private double a;//卖价格
    private double p;//最新价格
    private double h;//今日最高
    private double l;//今日最低

    private double c;//最后交易价格
    private int v;//最后交易尺寸
    private int t1;//总交易量
    private double w;//交易量加权平均价

    private Integer bidSize;
    private Integer askSize;//
}
