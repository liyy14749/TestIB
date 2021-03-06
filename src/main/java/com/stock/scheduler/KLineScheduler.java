package com.stock.scheduler;//package com.game.card.scheduler;

import com.stock.cache.DataCache;
import com.stock.core.config.PropConfig;
import com.stock.vo.KLineData;
import com.stock.vo.MktData;
import com.stock.vo.SymbolData;
import com.stock.vo.redisvo.KLineDataRedis;
import com.stock.vo.redisvo.MktDataRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class KLineScheduler {

    private static final Logger log = LoggerFactory.getLogger(KLineScheduler.class);

    @Autowired private RedisTemplate<String, Object> template;
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
                log.info("MktDataScheduler is run");
            }
        }).start();
    }

    public void work() {
    	while (true){
            try {
                Map<String, SymbolData> map= DataCache.symbolCache;
                for(String key:map.keySet()){
                    KLineData mktData = map.get(key).getKLineData();
                    if(mktData !=null){
                        long time = System.currentTimeMillis();
                        KLineDataRedis rd = new KLineDataRedis();
                        BeanUtils.copyProperties(mktData, rd);
                        rd.setT(time);
                        rd.setS(map.get(key).getContract().getSymbol());
                        StringBuilder sb = new StringBuilder("kline_1min_").append(key);
                        template.opsForZSet().add(sb.toString(), rd, time);
                    }
                }
            } catch (Exception e) {
                log.error("KLineScheduler work error", e);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error("KLineScheduler InterruptedException");
            }
        }
    }
}
