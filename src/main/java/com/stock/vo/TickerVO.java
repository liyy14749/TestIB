package com.stock.vo;

import lombok.Data;

@Data
public class TickerVO {
    private int tickerId;
    private ContractVO contract;

    public TickerVO(ContractVO contract) {
        this.contract = contract;
    }
}
