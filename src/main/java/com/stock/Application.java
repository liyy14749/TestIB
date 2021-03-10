package com.stock;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.stock.core.util.BeanUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@MapperScan("com.stock.dao")
@EnableScheduling
public class Application {

    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        new SpringApplicationBuilder().sources(Application.class).web(WebApplicationType.NONE).run(args);
        BeanUtil.getBean(SocketTask.class).start();
    }

}