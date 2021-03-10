package com.stock.scheduler;//package com.game.card.scheduler;

import com.alibaba.fastjson.JSON;
import com.stock.cache.DataCache;
import com.stock.core.config.PropConfig;
import com.stock.vo.MktDepth;
import com.stock.vo.SymbolData;
import com.stock.vo.redisvo.MktDepthRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class MktDepthScheduler {

    private static final Logger log = LoggerFactory.getLogger(MktDepthScheduler.class);

    @Autowired private RedisTemplate<String, String> template;
    @Autowired private PropConfig propConfig;

    @Value("${my.schedule.interval.depth}")
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
                    MktDepth mktData = map.get(key).getMktDepth();
                    if(mktData !=null){
                        long time = System.currentTimeMillis()/1000;
                        MktDepthRedis rd = new MktDepthRedis();
                        BeanUtils.copyProperties(mktData, rd);
                        rd.setTime(time);
                        rd.setSymbol(map.get(key).getContract().getSymbol());
                        TreeMap<Integer, Object[]> a = mktData.getAsk();
                        List<Object[]> aa = new ArrayList<>(a.size());
                        for(Integer position: a.keySet()){
                            aa.add(a.get(position));
                        }
                        TreeMap<Integer, Object[]> b = mktData.getBid();
                        List<Object[]> bb = new ArrayList<>(mktData.getBid().size());
                        for(Integer position: b.keySet()){
                            bb.add(b.get(position));
                        }
                        rd.setAsk(aa);
                        rd.setBid(bb);
                        String sb = String.format("depth_%s" ,key);
                        if(aa.size()>0 || bb.size()>0){
                            template.opsForZSet().add(sb, JSON.toJSONString(rd), time);
                        }
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
