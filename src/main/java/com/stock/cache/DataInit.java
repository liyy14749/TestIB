package com.stock.cache;

import com.alibaba.fastjson.JSON;
import com.stock.core.util.RedisUtil;
import com.stock.vo.ContractVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInit {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate<String, String> template;

    @PostConstruct
    public void init(){
        template.opsForList().trim("stock_static_symbol_us",1,0);
        template.opsForList().trim("stock_static_symbol_hk",1,0);
        List<String> usContracts = new ArrayList<>();
        usContracts.add(JSON.toJSONString(new ContractVO("TSLA","STK","USD","ISLAND", 1)));
        usContracts.add(JSON.toJSONString(new ContractVO("AAPL","STK","USD","ISLAND",2)));
        usContracts.add(JSON.toJSONString(new ContractVO("IBM","STK","USD","ISLAND",4)));
        template.opsForList().leftPushAll("stock_static_symbol_us", usContracts);

        List<String> hkContracts = new ArrayList<>();
        hkContracts.add(JSON.toJSONString(new ContractVO("9988","STK","HKD","SEHK",3)));
        hkContracts.add(JSON.toJSONString(new ContractVO("939","STK","HKD","SEHK",5)));
        hkContracts.add(JSON.toJSONString(new ContractVO("1810","STK","HKD","SEHK",6)));
        template.opsForList().leftPushAll("stock_static_symbol_hk", hkContracts);

        List<String> us = template.opsForList().range("stock_static_symbol_us",0,-1);
        List<ContractVO> usList= new ArrayList<>();
        for(String u:us){
            usList.add(JSON.parseObject(u,ContractVO.class));
        }
        DataCache.usContracts = usList;
        List<ContractVO> hkList= new ArrayList<>();
        List<String> hk = template.opsForList().range("stock_static_symbol_hk",0,-1);
        for(String h:hk){
            usList.add(JSON.parseObject(h,ContractVO.class));
        }
        DataCache.hkContracts = hkList;
    }
}
