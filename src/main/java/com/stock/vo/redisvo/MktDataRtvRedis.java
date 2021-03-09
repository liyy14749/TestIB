package com.stock.vo.redisvo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class MktDataRtvRedis {
    private String s;
    @JSONField(name = "T")
    private Long T;
    private double c;//最后交易价格
    private int v;//最后交易尺寸
    private double w;//交易量加权平均价
    private int t1;//交易总量

}
