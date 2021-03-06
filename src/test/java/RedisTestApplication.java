import com.alibaba.fastjson.JSON;
import com.stock.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class RedisTestApplication {
    public static String RANK = "rank:";
    @Autowired private RedisTemplate<String, String> template;
    @Autowired private RedisTemplate<String, Object> template2;

	@Test
	public void playerDetail() {
        template.opsForValue().set("a","a");
        System.out.println(template.opsForValue().get("a"));
        Map map = new HashMap();
        map.put("a","a");
        map.put("b","b");
        template2.opsForValue().set("b", JSON.toJSON(map));
        System.out.println(template2.opsForValue().get("b"));
	}
}
