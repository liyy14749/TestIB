package com.stock.thread;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;
import com.stock.core.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DealMsgThread {
    private static Logger log = LoggerFactory.getLogger(DealMsgThread.class);
    
    private static int queue_num = 15;
    private static BlockingQueue<MsgTask>[] taskQueues;
    // 工作线程
    private static WorkThread[] workThreads;

    @Autowired
    private RedisTemplate<String, String> template;
    @Autowired private RedisUtil redisUtil;

	@PostConstruct
    public void init() throws Exception {
        taskQueues = new BlockingQueue[queue_num];
    	workThreads = new WorkThread[queue_num];
        for(int j=0;j<queue_num;j++){
        	taskQueues[j] = new LinkedBlockingDeque<>();
        	workThreads[j] = new WorkThread(taskQueues[j]);
        	workThreads[j].setName("DealMsgThread-"+j);
            workThreads[j].start();// 开启线程池中的线程
        }
    }
    private long lastLogTime = 0;
    public void putTask(MsgTask task){
    	try {
    		//任务hash存放到 队列中
    		BlockingQueue<MsgTask> queue=taskQueues[task.getSymbolId().hashCode()%queue_num];
    		queue.put(task);
    		int size=queue.size();
    		if(size>10 && (System.currentTimeMillis()-lastLogTime)/1000>3){
        		log.warn("DealMsgThread queue size:"+queue.size());
                lastLogTime = System.currentTimeMillis();
        	}
		} catch (Exception e1) {
			log.error("putTask error");
		}
    }
    private class WorkThread extends Thread {
    	BlockingQueue<MsgTask> taskQueue;
    	public WorkThread(BlockingQueue<MsgTask> taskQueue){
    		this.taskQueue=taskQueue;
    	};
    	
        @Override
        public void run() {
            while (true) {
            	MsgTask task=null;
                try {
                	task = taskQueue.take();
                	if(task.getType() == 1){
                        TickTask tickTask = (TickTask) task;
                        Map<String,String> map = new HashMap<>();
                        for(int i=0;i<tickTask.getField().length;i++){
                            String field = tickTask.getField()[i];
                            map.put(field, String.valueOf(tickTask.getValue()[i]));
                        }
                        redisUtil.hashPutAll(tickTask.getRedisKey(),map);
                    } else if(task.getType() == 2){
                        ZSetTask klineTask = (ZSetTask) task;
                        template.opsForZSet().add(klineTask.getRedisKey(), JSON.toJSONString(klineTask.getObject()), klineTask.getTime());
                    } else if(task.getType() == 3){
                        TickField4Task tickField4Task = (TickField4Task) task;
                        String key = tickField4Task.getRedisKey();
                        Object price = tickField4Task.getValue();
                        Map<String,String> map = new HashMap<>();
                        map.put("last",String.valueOf(price));
                        map.put("close",String.valueOf(price));
                        String lastClose = redisUtil.hashGet(key,"last_close");
                        if(lastClose!=null){
                            BigDecimal price_change = new BigDecimal(String.valueOf(price)).subtract(new BigDecimal(lastClose));
                            map.put("price_change",String.valueOf(price_change));
                            BigDecimal percent = price_change.divide(new BigDecimal(lastClose) ,5 ,BigDecimal.ROUND_FLOOR);
                            map.put("price_change_percent",String.valueOf(percent));
                        }
                        redisUtil.hashPutAll(key,map);
                    }
                }
                catch (Throwable e) {
                    log.error("DealMsgThread 异常：" + e.getMessage(), e);
                }
            }
        }
    }
    
}
