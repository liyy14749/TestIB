package com.stock.cache;

import com.alibaba.fastjson.JSON;
import com.stock.core.util.RedisUtil;
import com.stock.vo.ContractVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInit {
    @Value("${spring.profiles.active}")
    private String env;
    @Autowired
    private RedisTemplate<String, String> template;

    @PostConstruct
    public void init(){
        String usKey = "stock_static_symbol_us";
        String ukKey = "stock_static_symbol_hk";
        if(env.equals("dev")){
            template.opsForList().trim(usKey,1,0);
            template.opsForList().trim(ukKey,1,0);
            List<String> usContracts = new ArrayList<>();
            usContracts.add(JSON.toJSONString(new ContractVO("TSLA","STK","USD","ISLAND", 100001)));
            usContracts.add(JSON.toJSONString(new ContractVO("AAPL","STK","USD","ISLAND",100002)));
            usContracts.add(JSON.toJSONString(new ContractVO("IBM","STK","USD","ISLAND",100004)));
            usContracts.add(JSON.toJSONString(new ContractVO("WMT","STK","USD","ISLAND",100013)));
            usContracts.add(JSON.toJSONString(new ContractVO("TSM","STK","USD","ISLAND",100011)));
            usContracts.add(JSON.toJSONString(new ContractVO("BABA","STK","USD","ISLAND",100010)));
            template.opsForList().leftPushAll(usKey, usContracts);

            List<String> hkContracts = new ArrayList<>();
            hkContracts.add(JSON.toJSONString(new ContractVO("9988","STK","HKD","SEHK",100003)));
            hkContracts.add(JSON.toJSONString(new ContractVO("939","STK","HKD","SEHK",100005)));
            hkContracts.add(JSON.toJSONString(new ContractVO("1810","STK","HKD","SEHK",100006)));
            template.opsForList().leftPushAll(ukKey, hkContracts);
        }

        List<String> us = template.opsForList().range(usKey,0,-1);
        List<ContractVO> usList= new ArrayList<>();
        for(String u:us){
            usList.add(JSON.parseObject(u,ContractVO.class));
        }
        DataCache.usContracts = usList;
        List<ContractVO> hkList= new ArrayList<>();
        List<String> hk = template.opsForList().range(ukKey,0,-1);
        for(String h:hk){
            usList.add(JSON.parseObject(h,ContractVO.class));
        }
        DataCache.hkContracts = hkList;
    }
}
