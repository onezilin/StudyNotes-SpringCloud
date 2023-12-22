package com.atguigu.springcloud;


import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan({"com.atguigu.springcloud.dao"})
// 开启 Seata 对数据源的代理
@EnableAutoDataSourceProxy
public class SeataStorageMain2003 {

    public static void main(String[] args) {
        SpringApplication.run(SeataStorageMain2003.class, args);
    }
}
