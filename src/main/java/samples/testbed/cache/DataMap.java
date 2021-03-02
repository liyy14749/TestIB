package samples.testbed.cache;

import samples.testbed.vo.MktData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataMap {
    public static Map<Integer, MktData> cache = new ConcurrentHashMap<>();

}
