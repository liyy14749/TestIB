package com.stock.scheduler;//package com.game.card.scheduler;

import com.alibaba.fastjson.JSON;
import com.stock.cache.DataCache;
import com.stock.core.config.PropConfig;
import com.stock.vo.MktData;
import com.stock.vo.SymbolData;
import com.stock.vo.redisvo.MktDataRedis;
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
public class MktDataScheduler {

    private static final Logger log = LoggerFactory.getLogger(MktDataScheduler.class);

    @Autowired private RedisTemplate<String, String> template;
    @Autowired private PropConfig propConfig;

    @Value("${my.schedule.interval.tick}")
    private Long interval;

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
                Map<Integer, SymbolData> map= DataCache.symbolCache;
                for(Integer key:map.keySet()){
                    MktData mktData = map.get(key).getMktData();
                    if(mktData !=null ){//&& !(mktData.getHigh()==0 && mktData.getLow() == 0)
                        long time = System.currentTimeMillis()/1000;
                        MktDataRedis rd = new MktDataRedis();
                        BeanUtils.copyProperties(mktData, rd);
                        rd.setTime(time/1000);
                        rd.setSymbol(map.get(key).getContract().getSymbol());
                        rd.setPrice_change(rd.getLast()-rd.getOpen());
                        if(rd.getOpen() !=0){
                            rd.setPrice_change_percent((rd.getLast()-rd.getOpen())/rd.getOpen());
                        }
                        String sb = String.format("tick_%s_v3", key);
                        template.opsForZSet().add(sb, JSON.toJSONString(rd), time);
                    }
                }
            } catch (Exception e) {
                log.error("MktDataScheduler work error", e);
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                log.error("MktDataScheduler InterruptedException");
            }
        }
    }
}
