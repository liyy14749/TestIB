package com.stock;

import com.stock.core.util.BeanUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.stock.dao")
@EnableScheduling
public class Application {

    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        new SpringApplicationBuilder().sources(Application.class).web(WebApplicationType.NONE).run(args);
        SocketTask.start(BeanUtil.getBean(EWrapperImpl.class));
    }

}