package com.stock.cache;

import com.alibaba.fastjson.JSON;
import com.stock.core.util.RedisUtil;
import com.stock.utils.KeyUtil;
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
    @Autowired
    private KeyUtil keyUtil;

    @PostConstruct
    public void init(){
        String usKey = keyUtil.getKeyWithPrefix("stock_static_symbol_us");
        String ukKey = keyUtil.getKeyWithPrefix("stock_static_symbol_hk");
        String indKey = keyUtil.getKeyWithPrefix("stock_static_symbol_ind");
        if(env.equals("dev")){
            template.opsForList().trim(ukKey,1,0);
            template.opsForList().trim(usKey,1,0);
            template.opsForList().trim(indKey,1,0);

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

            List<String> indContracts = new ArrayList<>();
            indContracts.add(JSON.toJSONString(new ContractVO("INDU","IND","USD","CME",1)));
            indContracts.add(JSON.toJSONString(new ContractVO("NDX","IND","USD","NASDAQ",2)));
            indContracts.add(JSON.toJSONString(new ContractVO("SPX","IND","USD","CBOE",3)));
            template.opsForList().leftPushAll(indKey, indContracts);
        }

        DataCache.usContracts = initCache(usKey);
        DataCache.hkContracts = initCache(ukKey);
        DataCache.indContracts = initCache(indKey);
    }

    private List<ContractVO> initCache(String indKey) {
        List<ContractVO> indList= new ArrayList<>();
        List<String> ind = template.opsForList().range(indKey,0,-1);
        for(String h:ind){
            indList.add(JSON.parseObject(h,ContractVO.class));
        }
        return indList;
    }
}
