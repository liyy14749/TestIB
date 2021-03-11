package com.stock.scheduler;//package com.game.card.scheduler;

import com.alibaba.fastjson.JSON;
import com.stock.cache.DataCache;
import com.stock.core.config.PropConfig;
import com.stock.vo.KLineData;
import com.stock.vo.MktData;
import com.stock.vo.SymbolData;
import com.stock.vo.redisvo.KLineDataRedis;
import com.stock.vo.redisvo.MktDataRedis;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class KLineScheduler {

    private static final Logger log = LoggerFactory.getLogger(KLineScheduler.class);

    @Autowired private RedisTemplate<String, String> template;
    @Autowired private PropConfig propConfig;

    private static boolean isRun = false;

    @Value("${my.schedule.interval.kline}")
    private Long interval;

    //@PostConstruct
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
                Map<Integer, SymbolData> map= DataCache.symbolCache;
                for(Integer key:map.keySet()){
                    KLineData kLineData = map.get(key).getKLineData();
                    if(kLineData !=null ){//&& !(kLineData.getHigh()==0 && kLineData.getLow()==0)
                        long time = System.currentTimeMillis()/1000;
                        KLineDataRedis rd = new KLineDataRedis();
                        BeanUtils.copyProperties(kLineData, rd);
                        rd.setTime(time);
                        rd.setSymbol(map.get(key).getContract().getSymbol());
                        String sb = String.format("kline_%s_5sec",key);
                        template.opsForZSet().add(sb, JSON.toJSONString(rd), time);
                    }
                }
            } catch (Exception e) {
                log.error("KLineScheduler work error", e);
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                log.error("KLineScheduler InterruptedException");
            }
        }
    }
}
