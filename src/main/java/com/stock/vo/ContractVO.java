package com.stock.vo;

import cn.hutool.core.date.DateUtil;
import com.ib.client.Contract;
import com.stock.core.util.DateTimeUtil;
import lombok.Data;

import java.util.Date;

@Data
public class ContractVO {
    private String symbol;
    private String secType;
    private String currency;
    private String exchange;
    private Integer symbolId;
    private String primaryExch;
    private Contract contract;
    private long timeFrom;
    private long timeTo;
    private String dateFrom;
    private String dateTo;

    public ContractVO() {
    }

    public ContractVO(String symbol, String secType, String currency, String exchange, Integer symbolId) {
        this.symbol = symbol;
        this.secType = secType;
        this.currency = currency;
        this.exchange = exchange;
        this.symbolId = symbolId;
        this.timeFrom = 0L;
        this.timeTo = DateUtil.offsetDay(new Date(), 365).getTime()/1000;
    }

}
