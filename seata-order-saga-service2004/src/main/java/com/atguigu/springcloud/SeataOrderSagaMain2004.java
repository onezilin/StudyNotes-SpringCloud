package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SeataOrderSagaMain2004 {

    public static void main(String[] args) {
        SpringApplication.run(SeataOrderSagaMain2004.class, args);
    }
}
