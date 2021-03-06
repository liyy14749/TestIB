package com.stock.vo;

import lombok.Data;

@Data
public class TickerVO {
    private int tickerId;
    private String key;
    private ContractVO contract;

    public TickerVO(String key,ContractVO contract) {
        this.contract = contract;
        this.key = key;
    }
}
