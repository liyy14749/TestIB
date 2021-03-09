package com.stock.vo;

import lombok.Data;

@Data
public class MktData {
    private Long T;
    private double b;//买价格
    private double a;//卖价格
    private double p;//最新价格
    private double h;//今日最高
    private double l;//今日最低

    private Integer bidSize;
    private Integer askSize;//
}
