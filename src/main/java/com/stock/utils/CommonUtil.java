package com.stock.utils;

import com.stock.vo.ContractVO;

public class CommonUtil {

    public static boolean isValidTime(ContractVO contractVO) {
        long curTime = System.currentTimeMillis()/1000;
        if(!(contractVO.getTimeFrom()==0 && contractVO.getTimeTo()==0) && (curTime>= contractVO.getTimeFrom() && curTime<= contractVO.getTimeTo())){
            return true;
        }
        return false;
    }
}
