package com.stock.vo.redisvo;

import lombok.Data;

@Data
public class LatestDealRedis {
    private long time;
    private double price;
    private long volume;
    private boolean maker;
}