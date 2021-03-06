package com.stock.vo;

import lombok.Data;

@Data
public class MktData {
    private Long T;
    private Double b;//买价格
    private Double a;//卖价格
    private Double p;//最新价格
    private Double h;//今日最高
    private Double l;//今日最低

    private Integer bidSize;
    private Integer askSize;//
}
