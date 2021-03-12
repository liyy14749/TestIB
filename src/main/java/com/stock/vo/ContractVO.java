package com.stock.vo;

import lombok.Data;

@Data
public class ContractVO {
    private String symbol;
    private String secType;
    private String currency;
    private String exchange;
    private Integer symbolId;
    private String primaryExch;

    public ContractVO(String symbol, String secType, String currency, String exchange, Integer symbolId) {
        this.symbol = symbol;
        this.secType = secType;
        this.currency = currency;
        this.exchange = exchange;
        this.symbolId = symbolId;
    }

}
