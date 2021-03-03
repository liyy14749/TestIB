package com.stock.vo;

public class TickerVO {
    private int tickerId;
    private String symbol;

    public TickerVO(int tickerId, String symbol) {
        this.tickerId = tickerId;
        this.symbol = symbol;
    }

    public int getTickerId() {
        return tickerId;
    }

    public void setTickerId(int tickerId) {
        this.tickerId = tickerId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
