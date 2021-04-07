import com.alibaba.fastjson.JSON;
import com.stock.vo.ContractVO;
import com.stock.vo.MktDepth;
import com.stock.vo.SymbolData;
import com.stock.vo.redisvo.MktDepthRedis;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestAll {
    public static void main(String[] args) {
//        Map<String, SymbolData> map = Collections.synchronizedMap(new LinkedHashMap<>());
//        map.put("a",new SymbolData());
//        map.put("b",new SymbolData());
//        map.put("d",new SymbolData());
//        map.put("e",new SymbolData());
//        map.put("c",new SymbolData());
//        for(String key:map.keySet()){
//            System.out.println(key);
//        }

//        MktDepth mktData = new MktDepth();
//        TreeMap<Integer, Object[]> map = new TreeMap<>();
//        map.put(1,new Object[]{1,2});
//        mktData.setAsk(map);
//        MktDepthRedis rd = new MktDepthRedis();
//        BeanUtils.copyProperties(mktData, rd);
//        rd.setTime(System.currentTimeMillis());
//        System.out.println(JSON.toJSONString(rd));
//        double a=0.0;
//        long time = System.currentTimeMillis()/1000;
//        System.out.println(time/5);
        String s = "{\"currency\":\"USD\",\"exchange\":\"ISLAND\",\"secType\":\"STK\",\"symbol\":\"BABA\",\"symbolId\":100010,\"timeFrom\":111111,\"timeTo\":1649337511}";
        System.out.println(JSON.parseObject(s, ContractVO.class));
//        AtomicBoolean a = new AtomicBoolean(true);
//        a.set(false);
//        System.out.println(a);
    }
}
