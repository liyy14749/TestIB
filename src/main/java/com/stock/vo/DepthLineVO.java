package com.stock.vo;

import lombok.Data;

@Data
public class DepthLineVO {

    private Double price;
    private Integer size;

    public DepthLineVO(Double price, Integer size) {
        this.price = price;
        this.size = size;
    }
}
