package com.atguigu.springcloud;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan({"com.atguigu.springcloud.dao"})
public class SeataStorageMain2003 {

    public static void main(String[] args) {
        SpringApplication.run(SeataStorageMain2003.class, args);
    }
}
