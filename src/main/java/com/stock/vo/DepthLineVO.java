package com.stock.vo;

public class DepthLineVO {

    private Double price;
    private Integer size;
    private Integer position;

    public DepthLineVO(Double price, Integer size, Integer position) {
        this.price = price;
        this.size = size;
        this.position = position;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
