package com.stock.vo.redisvo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MktDepthRedis {
    private String symbol;
    private Long time;
    private List<Object[]> ask = new ArrayList<>();
    private List<Object[]> bid = new ArrayList<>();
}
