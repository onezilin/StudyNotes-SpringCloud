package com.atguigu.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: 自定义的负载均衡算法
 * 注意：官方文档明确给出了警告：这个自定义配置类不能放在 @ComponentScan 所扫描的当前包下以及子包下，
 * 否则我们自定义的这个配置类就会被所有的 Ribbon 客户端所共享，也就是说我们达不到特殊化定制的目的了。
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule() {
        return new RandomRule(); // 定义为随机
    }
}
