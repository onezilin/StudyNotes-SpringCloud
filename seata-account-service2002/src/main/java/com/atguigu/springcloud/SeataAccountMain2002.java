package com.atguigu.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Description:
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan({"com.atguigu.springcloud.dao"})
public class SeataAccountMain2002 {

    public static void main(String[] args) {
        SpringApplication.run(SeataAccountMain2002.class, args);
    }
}
