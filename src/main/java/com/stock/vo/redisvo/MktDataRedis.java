package com.stock.vo.redisvo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class MktDataRedis {
    private String s;
    @JSONField(name = "T")
    private Long T;
    private double b;//买价格
    private double a;//卖价格
    private double p;//最新价格
    private double h;//今日最高
    private double l;//今日最低

}
