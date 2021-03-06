package com.stock.vo;

import lombok.Data;

@Data
public class SymbolData {
    private MktData mktData;
    private MktDepth mktDepth;
    private KLineData kLineData;
    private ContractVO contract;
}
