package com.atguigu.springcloud;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
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
// 开启 Seata 对数据源的代理，指定数据源代理为 XA 模式
// 测试步骤：调用 business 接口，查看 undo_log 表，如果没有 before image、after image 等中间数据，则说明为 XA 模式
@EnableAutoDataSourceProxy(dataSourceProxyMode = "XA")
public class SeataAccountMain2002 {

    public static void main(String[] args) {
        SpringApplication.run(SeataAccountMain2002.class, args);
    }
}
