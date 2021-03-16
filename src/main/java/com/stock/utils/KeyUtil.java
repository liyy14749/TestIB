package com.stock.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyUtil {

    @Value("${spring.profiles.active}")
    private String env;

    public String getKeyWithPrefix(String key){
        if(env.equals("test")){
            return String.format("test_%s",key);
        }
        return key;
    }
}
