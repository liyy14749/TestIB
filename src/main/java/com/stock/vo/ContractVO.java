package com.stock.vo;

public class ContractVO {
    private String symbol;
    private String secType;
    private String currency;
    private String exchange;

    public ContractVO(String symbol, String secType, String currency, String exchange) {
        this.symbol = symbol;
        this.secType = secType;
        this.currency = currency;
        this.exchange = exchange;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecType() {
        return secType;
    }

    public void setSecType(String secType) {
        this.secType = secType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
