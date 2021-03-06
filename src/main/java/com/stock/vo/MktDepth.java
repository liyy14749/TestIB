package com.stock.vo;

import lombok.Data;

import java.util.TreeMap;

@Data
public class MktDepth {
    private Long T;
    private TreeMap<Integer,Object[]> a = new TreeMap<>();
    private TreeMap<Integer,Object[]> b = new TreeMap<>();
}
