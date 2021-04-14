package com.stock.cache;

import com.alibaba.fastjson.JSON;
import com.stock.utils.KeyUtil;
import com.stock.vo.ContractVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInit {
    private static Logger log = LoggerFactory.getLogger(DataInit.class);
    @Value("${spring.profiles.active}")
    private String env;
    @Value("#{'${my.stock.redisKey}'.split(',')}")
    private List<String> stockKeys;
    @Autowired
    private RedisTemplate<String, String> template;
    @Autowired
    private KeyUtil keyUtil;
    private long lastLoadTime = 0;

    @PostConstruct
    public void init(){
        readRedis();
    }

    public void reloadRedis(){
        if((System.currentTimeMillis() - lastLoadTime)/1000 >= 3600){
            readRedis();
            lastLoadTime = System.currentTimeMillis();
            log.info("reloadRedis");
        }
    }

    public void readRedis(){
        if(stockKeys!=null && stockKeys.size()>0){
            String devKey = keyUtil.getKeyWithPrefix(stockKeys.get(0));
            if(env.equals("dev")){
                template.opsForList().trim(devKey,1,0);

                List<String> usContracts = new ArrayList<>();
                usContracts.add(JSON.toJSONString(new ContractVO("TSLA","STK","USD","ISLAND", 100001)));
                usContracts.add(JSON.toJSONString(new ContractVO("AAPL","STK","USD","ISLAND",100002)));
                usContracts.add(JSON.toJSONString(new ContractVO("IBM","STK","USD","ISLAND",100004)));
                usContracts.add(JSON.toJSONString(new ContractVO("WMT","STK","USD","ISLAND",100013)));
                usContracts.add(JSON.toJSONString(new ContractVO("TSM","STK","USD","ISLAND",100011)));
                usContracts.add(JSON.toJSONString(new ContractVO("BABA","STK","USD","ISLAND",100010)));
                usContracts.add(JSON.toJSONString(new ContractVO("AAPL","STK","USD","ISLAND",100011)));
                usContracts.add(JSON.toJSONString(new ContractVO("BILI","STK","USD","ISLAND",100012)));
                usContracts.add(JSON.toJSONString(new ContractVO("DOYU","STK","USD","ISLAND",100013)));
                usContracts.add(JSON.toJSONString(new ContractVO("IQ","STK","USD","ISLAND",100014)));
                usContracts.add(JSON.toJSONString(new ContractVO("GME","STK","USD","ISLAND",100015)));
                usContracts.add(JSON.toJSONString(new ContractVO("FB","STK","USD","ISLAND",100016)));
                usContracts.add(JSON.toJSONString(new ContractVO("GOOG","STK","USD","ISLAND",100017)));
                usContracts.add(JSON.toJSONString(new ContractVO("TSLA","STK","USD","ISLAND",100018)));
                usContracts.add(JSON.toJSONString(new ContractVO("BIDU","STK","USD","ISLAND",100019)));
                usContracts.add(JSON.toJSONString(new ContractVO("BABA","STK","USD","ISLAND",100020)));
                usContracts.add(JSON.toJSONString(new ContractVO("GSX","STK","USD","ISLAND",100021)));
                usContracts.add(JSON.toJSONString(new ContractVO("MSFT","STK","USD","ISLAND",100022)));
                usContracts.add(JSON.toJSONString(new ContractVO("AMZN","STK","USD","ISLAND",100023)));
                usContracts.add(JSON.toJSONString(new ContractVO("TSM","STK","USD","ISLAND",100024)));
                usContracts.add(JSON.toJSONString(new ContractVO("JPM","STK","USD","ISLAND",100025)));
                usContracts.add(JSON.toJSONString(new ContractVO("V","STK","USD","ISLAND",100026)));
                usContracts.add(JSON.toJSONString(new ContractVO("JNJ","STK","USD","ISLAND",100027)));
                usContracts.add(JSON.toJSONString(new ContractVO("WMT","STK","USD","ISLAND",100028)));
                usContracts.add(JSON.toJSONString(new ContractVO("MA","STK","USD","ISLAND",100029)));
                usContracts.add(JSON.toJSONString(new ContractVO("NVDA","STK","USD","ISLAND",100030)));
                usContracts.add(JSON.toJSONString(new ContractVO("UNH","STK","USD","ISLAND",100031)));
                usContracts.add(JSON.toJSONString(new ContractVO("BAC","STK","USD","ISLAND",100032)));
                usContracts.add(JSON.toJSONString(new ContractVO("HD","STK","USD","ISLAND",100033)));
                usContracts.add(JSON.toJSONString(new ContractVO("DIS","STK","USD","ISLAND",100034)));
                usContracts.add(JSON.toJSONString(new ContractVO("PG","STK","USD","ISLAND",100035)));
                usContracts.add(JSON.toJSONString(new ContractVO("PYPL","STK","USD","ISLAND",100036)));
                usContracts.add(JSON.toJSONString(new ContractVO("INTC","STK","USD","ISLAND",100037)));
                usContracts.add(JSON.toJSONString(new ContractVO("ASML","STK","USD","ISLAND",100038)));
                usContracts.add(JSON.toJSONString(new ContractVO("NFLX","STK","USD","ISLAND",100039)));
                usContracts.add(JSON.toJSONString(new ContractVO("CMCSA","STK","USD","ISLAND",100040)));
                usContracts.add(JSON.toJSONString(new ContractVO("ADBE","STK","USD","ISLAND",100041)));
                usContracts.add(JSON.toJSONString(new ContractVO("VZ","STK","USD","ISLAND",100042)));
                usContracts.add(JSON.toJSONString(new ContractVO("XOM","STK","USD","ISLAND",100043)));
                usContracts.add(JSON.toJSONString(new ContractVO("KO","STK","USD","ISLAND",100044)));
                usContracts.add(JSON.toJSONString(new ContractVO("CSCO","STK","USD","ISLAND",100045)));
                usContracts.add(JSON.toJSONString(new ContractVO("ORCL","STK","USD","ISLAND",100046)));
                usContracts.add(JSON.toJSONString(new ContractVO("TM","STK","USD","ISLAND",100047)));
                usContracts.add(JSON.toJSONString(new ContractVO("T","STK","USD","ISLAND",100048)));
                usContracts.add(JSON.toJSONString(new ContractVO("ABT","STK","USD","ISLAND",100049)));
                usContracts.add(JSON.toJSONString(new ContractVO("CHT","STK","USD","ISLAND",100050)));

                usContracts.add(JSON.toJSONString(new ContractVO("9988","STK","HKD","SEHK",100003)));
                usContracts.add(JSON.toJSONString(new ContractVO("939","STK","HKD","SEHK",100005)));
                usContracts.add(JSON.toJSONString(new ContractVO("1810","STK","HKD","SEHK",100006)));

                usContracts.add(JSON.toJSONString(new ContractVO("INDU","IND","USD","CME",1)));
                usContracts.add(JSON.toJSONString(new ContractVO("NDX","IND","USD","NASDAQ",2)));
                usContracts.add(JSON.toJSONString(new ContractVO("SPX","IND","USD","CBOE",3)));
                template.opsForList().leftPushAll(devKey, usContracts);
            }

            for(String key:stockKeys){
                String usKey = keyUtil.getKeyWithPrefix(key);
                DataCache.contracts.addAll(initCache(usKey));
            }
        } else {
            log.warn("my.stock.redisKey is empty");
        }
    }

    private List<ContractVO> initCache(String indKey) {
        List<ContractVO> indList= new ArrayList<>();
        List<String> ind = template.opsForList().range(indKey,0,-1);
        if(ind!=null && ind.size()>0){
            for(String h:ind){
                indList.add(JSON.parseObject(h,ContractVO.class));
            }
        } else {
            log.warn(String.format("redis key %s is empty" , indKey));
        }
        return indList;
    }
}
