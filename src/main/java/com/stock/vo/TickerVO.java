package com.stock.vo;

import lombok.Data;

@Data
public class TickerVO {
    private int tickerId;
    private ContractVO contract;
    private int type;//1-tick,2-depth,3-kline

    public TickerVO(ContractVO contract,int type) {
        this.contract = contract;
        this.type = type;
    }
}
