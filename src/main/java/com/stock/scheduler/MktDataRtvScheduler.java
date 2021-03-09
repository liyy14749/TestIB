package com.stock.scheduler;//package com.game.card.scheduler;

import com.alibaba.fastjson.JSON;
import com.stock.cache.DataCache;
import com.stock.core.config.PropConfig;
import com.stock.vo.MktData;
import com.stock.vo.SymbolData;
import com.stock.vo.redisvo.MktDataRedis;
import com.stock.vo.redisvo.MktDataRtvRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class MktDataRtvScheduler {

    private static final Logger log = LoggerFactory.getLogger(MktDataRtvScheduler.class);

    @Autowired private RedisTemplate<String, String> template;
    @Autowired private PropConfig propConfig;

    private static boolean isRun = false;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            if(!isRun){
                isRun=true;
                work();
                isRun=false;
            } else {
                log.info("MktDataRtvScheduler is run");
            }
        }).start();
    }

    public void work() {
    	while (true){
            try {
                Map<String, SymbolData> map= DataCache.symbolCache;
                for(String key:map.keySet()){
                    MktData mktData = map.get(key).getMktData();
                    if(mktData !=null && !(mktData.getT1() ==0)){
                        long time = System.currentTimeMillis();
                        MktDataRtvRedis rd = new MktDataRtvRedis();
                        BeanUtils.copyProperties(mktData, rd);
                        rd.setT(time);
                        rd.setS(map.get(key).getContract().getSymbol());
                        StringBuilder sb = new StringBuilder("mkt_data_rtv_").append(key);
                        template.opsForZSet().add(sb.toString(), JSON.toJSONString(rd), time);
                    }
                }
            } catch (Exception e) {
                log.error("MktDataRtvScheduler work error", e);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("MktDataRtvScheduler InterruptedException");
            }
        }
    }
}
