package com.atguigu.springcloud.filter;

import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * Description: 自定义局部过滤器工厂，并且注入到 spring 容器中，自定义过滤器需要加入到过滤器工厂
 */
@Component
public class MyPartialFilterFactory extends AbstractGatewayFilterFactory<Object> {

    /**
     * Description: 指定自定义局部过滤器
     *
     * @param config 配置文件中传入的参数，写法可以参考 Gateway 提供的过滤器工厂，例如：AddRequestHeaderGatewayFilterFactory
     */
    @Override
    public MyPartialFilter apply(Object config) {
        System.out.println(config);
        return new MyPartialFilter();
    }

    /**
     * Description: 指定过滤器的名称
     */
    @Override
    public String name() {
        return "MyPartialFilter";
    }
}
