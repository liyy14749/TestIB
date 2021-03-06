import com.stock.vo.MktDepth;
import com.stock.vo.SymbolData;
import com.stock.vo.redisvo.MktDepthRedis;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

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

        MktDepth mktData = new MktDepth();
        TreeMap<Integer, Object[]> map = new TreeMap<>();
        map.put(1,new Object[]{1,2});
        mktData.setA(map);
        MktDepthRedis rd = new MktDepthRedis();
        BeanUtils.copyProperties(mktData, rd);
        System.out.println(rd);
        System.out.println(mktData);
    }
}
