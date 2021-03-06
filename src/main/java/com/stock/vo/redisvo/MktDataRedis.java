package com.stock.vo.redisvo;

import lombok.Data;

@Data
public class MktDataRedis {
    private String s;
    private Long T;
    private Double b;//买价格
    private Double a;//卖价格
    private Double p;//最新价格
    private Double h;//今日最高
    private Double l;//今日最低

}
