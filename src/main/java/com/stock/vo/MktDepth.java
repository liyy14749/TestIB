package com.stock.vo;

import lombok.Data;

import java.util.TreeMap;

@Data
public class MktDepth {
    private TreeMap<Integer,Object[]> ask = new TreeMap<>();
    private TreeMap<Integer,Object[]> bid = new TreeMap<>();
}
