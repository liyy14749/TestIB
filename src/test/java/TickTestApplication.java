import com.alibaba.fastjson.JSON;
import com.ib.client.TickAttrib;
import com.stock.Application;
import com.stock.EWrapperImpl;
import com.stock.SocketTask;
import com.stock.core.util.BeanUtil;
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
public class TickTestApplication {
    @Autowired private com.stock.EWrapperImpl eWrapperImpl;

	@Test
	public void playerDetail() {
        BeanUtil.getBean(SocketTask.class).start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        eWrapperImpl.tickPrice(1,0,10,new TickAttrib());
	}
}
