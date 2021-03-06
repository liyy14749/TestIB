package com.stock.vo.redisvo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MktDepthRedis {
    private String s;
    private Long T;
    private List<Object[]> a = new ArrayList<>();
    private List<Object[]> b = new ArrayList<>();
}
