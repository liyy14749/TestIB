package com.stock.vo;

import lombok.Data;

@Data
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

}
