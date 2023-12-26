# SpringCloud 项目模块结构及功能

## 一、服务注册发现中心

### （一）Eureka

#### 1、cloud-provider-payment8001、cloud-provider-payment8002

服务提供者，使用 Eureka Client 注册到 Eureka 服务注册中心：

- @EnableDiscoveryClient 将服务注册到服务注册发现中心。

```xml
<dependecies>
    <!-- 服务注册中心的客户端 eureka-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- Actuator 监控 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependecies>
```

```yml
server:
  port: 8001

spring:
  application:
    name: cloud-payment-service # Eureka Server 中的展示的服务别名，相同的别名对外被看成一个应用

eureka:
  client:
    register-with-eureka: true # 是否注册进 eureka 服务注册中心
    fetch-registry: true # 是否从 eureka 服务注册中心抓取已有的注册信息
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
  instance:
    instance-id: payment8001 # 展示的主机名称
    prefer-ip-address: true # 查看主机名称是时左下角展示 ip 地址
```

#### 2、cloud-provider-payment7001、cloud-provider-payment7002

Eureka Server 端，无实际业务：

```xml
<dependecies>
    <!-- 服务注册中心的服务端 eureka-server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>

    <!-- Actuator 监控 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependecies>
```

```yml
server:
  port: 7001

eureka:
  instance:
    hostname: eureka7001.com # eureka 服务注册中心实例名称，集群中唯一
  client:
    register-with-eureka: false # 不向注册中心注册自己
    fetch-registry: false # 声明自己是服务注册中心，eureka 服务注册中心只需要维护服务实例，不需要去检索服务
    service-url:
      # defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
      defaultZone: http://eureka7002.com:7002/eureka/ # 集群模式下值为其他 Eureka Server 地址，中间逗号 "," 隔开
```

### （二）Zookeeper

#### 1、cloud-provider-payment8004

使用 Zookeeper 作为服务注册发现中心：

```xml
<dependecies>
    <!-- SpringBoot整合zookeeper客户端 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
        <!--先排除自带的zookeeper3.5.3-->
        <exclusions>
            <exclusion>
                <groupId>org.apache.zookeeper</groupId>
                <artifactId>zookeeper</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <!--添加 Zookeeper 3.6.3 版本-->
    <dependency>
        <groupId>org.apache.zookeeper</groupId>
        <artifactId>zookeeper</artifactId>
        <version>3.6.3</version>
    </dependency>
</dependecies>
```

```yml
server:
  port: 8004

spring:
  application:
    name: cloud-provider-payment # 服务别名----注册 Zookeeper 到注册中心名称
  cloud:
    zookeeper:
      # TODO: 自定义，逗号 "," 隔开
      connect-string: 192.168.190.134:2181,192.168.190.134:2182,192.168.190.134:2183
      # 是否将本服务注册到 Zookeeper，默认值为 true，和 @EnableDiscoveryClient 作用相同，也就是默认就会注册到 Zookeeper，该配置优先级更大
      discovery.enabled: true
```

### （三）Consul

#### 1、cloud-provider-payment8006

使用 Consul 作为服务注册发现中心：

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

```yml
server:
  port: 8006

spring:
  application:
    name: consul-provider-payment
  cloud:
    consul:
      # TODO: 自定义，consul 地址
      host: 192.168.190.134
      port: 8500
      discovery:
        service-name: ${spring.application.name}
```

### （四）Nacos

#### 1、cloudalibaba-provider-nacos-payment9001、cloudalibaba-provider-nacos-payment9002

服务提供者，使用 Nacos 作为服务注册发现中心。

- 测试 cluster-name 的作用。

```xml
<!-- Nacos 服务发现依赖 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>2021.1</version>
</dependency>
```

```yml
server:
  port: 9001

spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        ephemeral: true # 是否是临时实例，true 表示临时实例，false 表示持久化实例
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
        cluster-name: one

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

#### 2、cloudalibaba-consumer-nacos-order83

服务消费端，将 Nacos 看做服务注册发现中心：

- 使用 RestTemplate + @LoadBalanced 负载均衡的方式获取 Nacos 中注册的服务。

```xml
<!-- Nacos 服务发现依赖 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <!-- <version>2021.1</version>-->
    <version>2.2.5.RELEASE</version>
</dependency>
```

```yml
server:
  port: 83

spring:
  application:
    name: nacos-order-consumer
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
        cluster-name: one

nacos-payment-provider:
  ribbon:
    NFLoadBalancerRuleClassName: com.alibaba.cloud.nacos.ribbon.NacosRule # 指定 Ribbon 负载均衡策略
```

## 二、负载均衡

### （一）Ribbon

#### 1、cloud-consumer-order80

服务消费者：

- 直接使用 RestTemplate 请求指定 IP 端口的服务。
- 使用 RestTemplate + @LoadBalanced 负载均衡的方式获取 Eureka 中注册的服务。
- 使用 @RibbonClient 指定负载均衡方法。
- 自定义 LoadBalanced 使用的负责均衡方法及使用。

```xml
<dependecies>
    <!-- 服务注册中心的客户端 eureka-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- Actuator 监控 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependecies>
```

```yml
server:
  port: 80

spring:
  application:
    name: cloud-order-service # Eureka Server 中的展示的服务别名

eureka:
  client:
    register-with-eureka: true # 是否注册进 eureka 服务注册中心
    fetch-registry: true # 是否从 eureka 服务注册中心抓取已有的注册信息
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
  instance:
    instance-id: order80 # 展示的主机名称
    prefer-ip-address: true # 查看主机名称是时左下角展示 ip 地址
```

### （二）OpenFeign

#### 1、cloud-consumer-feign-order80

服务消费者：

- 使用 @EnableFeignClients 开启 Feign 功能。
- 使用 @FeignClient 配置 Feign 功能（包括服务降级功能），它对 RestTemplate + Ribbon 进行了封装，可以对 @FeignClient 进行自定义配置。
- 原生 Feign 的使用。

```xml
<dependecies>
    <!-- OpenFeign 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!-- 原生 Feign 使用到的依赖 -->
    <dependency>
        <groupId>com.netflix.feign</groupId>
        <artifactId>feign-jackson</artifactId>
        <version>8.18.0</version>
    </dependency>

    <!-- 服务注册中心的客户端 eureka-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependecies>
```

```shell
server:
  port: 80

eureka:
  client:
    # 不注册进eureka服务中心，不需要 @EnableEurekaClient
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/

feign:
  # 设置
  client:
    config:
      default:
        # Feign 调用连接超时时间
        read-timeout: 5000
        # Feign 调用请求超时时间
        connect-timeout: 5000
  # 开启 feign.hystrix.enable，使 fallback 生效
  hystrix:
    enabled: true

# 开启 feign.hystrix.enable，使 fallback 生效，但是会使 read-timeout 和 connect-timeout 失效，需要在使用以下配置
hystrix:
  command:
    default: # default全局有效，service id指定应用有效
      execution:
        timeout:
          enabled: true # 如果enabled设置为false，则请求超时交给ribbon控制；为true，则超时作为熔断根据
        isolation:
          thread:
            timeoutInMilliseconds: 5000 # 断路器超时时间，默认10000ms
            # timeoutInMilliseconds: 1000 # 测试 fallback 时，设置为 1s

logging:
  level:
    # 为指定的 @FeignClient 配置日志
    com.atguigu.springcloud.service.PaymentFeignServiceConfigurationTest: DEBUG
```

## 三、服务降级

### （一）Hystrix

#### 1、cloud-provider-hystrix-payment8001

服务提供者：

- HystrixCommand 与 HystrixObservableCommand 代码方式配置服务端服务降级。
- @HystrixCommand 注解方式配置服务端服务降级，@EnableCircuitBreaker 开启服务熔断。

```xml
<dependecies>
    <!-- hystrix 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>

    <!-- 服务注册中心的客户端 eureka-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependecies>
```

```yml
server:
  port: 8001

spring:
  application:
    name: cloud-provider-hystrix-payment

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka

hystrix:
  command:
    # 全局默认配置
    default:
      execution.isolation.thread.timeoutInMilliseconds: 3000
```

#### 2、cloud-consumer-feign-hystrix-order80

服务消费者：

- 使用 @EnableFeignClients 开启 Feign 功能。
- 使用 @FeignClient 配置 Feign 功能（包括消费端服务降级功能），@EnableCircuitBreaker 开启服务熔断。

```xml
<dependecies>
    <!-- hystrix 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>

    <!-- OpenFeign 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!-- 服务注册中心的客户端 eureka-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependecies>
```

```yml
server:
  port: 80

eureka:
  client:
    # 不注册进eureka服务中心，不需要 @EnableEurekaClient
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/

feign:
  # 设置
  client:
    config:
      default:
        # Feign 调用连接超时时间
        read-timeout: 5000
        # Feign 调用请求超时时间
        connect-timeout: 5000
  hystrix:
    enabled: true

hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 7000 # 超时时间

# Feign（或 Ribbon）超时时间同时配置时，取两者之间的最小值
```

#### 3、cloud-consumer-hystrix-dashboard9001

Hystrix Dashboard 可以用于统计经过 Hystrix 的请求，需要修改 cloud-provider-hystrix-payment8001 启动类。

```xml
<dependecies>
    <!-- hystrix dashboard 依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
    </dependency>

    <!-- Actuator 依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependecies>
```

### （二）Sentinel

#### 1、cloudalibaba-sentinel-service8401、cloudalibaba-sentinel-service8402

- 使用编码方式设置 Sentinel 资源及规则。
- 使用 Sentinel Dashborder 方式设置 Sentinel 资源及规则。
- 使用 SPI 方式自定义 Slot 责任链。

```xml
<dependecies>
    <!--SpringCloud ailibaba sentinel -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        <version>2021.1</version>
        <exclusions>
            <exclusion>
                <groupId>com.alibaba.csp</groupId>
                <artifactId>sentinel-core</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <!-- 由于部分功能的兼容问题，自己引入相关依赖 -->
    <!-- Sentinel core -->
    <dependency>
        <groupId>com.alibaba.csp</groupId>
        <artifactId>sentinel-core</artifactId>
        <version>${sentinel.version}</version>
    </dependency>
    <!-- Sentinel 项目和 Console 控制台通信依赖-->
    <dependency>
        <groupId>com.alibaba.csp</groupId>
        <artifactId>sentinel-transport-simple-http</artifactId>
        <version>${sentinel.version}</version>
    </dependency>
</dependecies>
```

```yml
server:
  port: 8401

spring:
  application:
    name: cloudalibaba-sentinel-service
  cloud:
    sentinel:
      transport:
        dashboard: 192.168.190.134:8080 # TODO: 配置 Sentinel 控制台地址及端口号
        port: 8719 # Sentinel 控制台和客户端通信的端口号，不配置的话默认是 8719，如果被占用了会自动往上加 1 找一个未被占用的端口
        client-ip: 192.168.1.122 # TODO: 配置 Sentinel 客户端 IP（也就是本机 IP），不配置可能会识别成 Sentinel 控制台所在的 IP，导致错误
      web-context-unify: false # 开启 STRATEGY_CHAIN 链路流控模式，详见 issue #1213

management:
  endpoints:
    web:
      exposure:
        include: "*" # 暴露所有端点，否则 Sentinel 控制台无法监控到 /actuator/sentinel 端点
```

### （三）Sentinel 集成 Ribbon、Feign、Nacos

#### 1、cloudalibaba-provider-nacos-payment9003、cloudalibaba-provider-nacos-payment9004

服务提供者，使用 Nacos 作为服务注册发现中心。

```xml
<!-- nacos 服务发现依赖 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

```yml
server:
  port: 9003

spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

#### 2、cloudalibaba-consumer-nacos-order84

服务消费者：

- 使用 Nacos 作为服务注册发现中心。
- Sentinel 搭配 Ribbon，使用 @SentinelResource 实现客户端的服务降级。
- Sentinel 搭配 Feign，使用 @FeignClient 实现客户端的服务降级。
- Sentinel 搭配 Nacos，实现规则持久化。

```xml
<dependecies>
    <!-- openfeign 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!-- nacos 服务发现依赖 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>

    <!-- sentinel 依赖 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    </dependency>
</dependecies>
```

#### 3、cloudalibaba-sentinel-service8401

还是使用 cloudalibaba-sentinel-service8401 模块，查看 Sentinel 搭配 Nacos 的效果：

```xml
<!-- Nacos 动态规则扩展数据源依赖 -->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

```yml
spring:
  cloud:
    sentinel:
      datasource:
        myFlowRule1: # 自定义数据源名称，任意
          nacos:
            server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
            namespace: myCustomNamespaceID # 命名空间 namespace ID，读取此 namespace 下的配置
            group-id: CUSTOM_GROUP # 组名
            data-id: ${spring.application.name}-flow-rules # 指定配置文件名
            data-type: json # 指定配置文件类型
            rule-type: flow # 指定规则类型
```

Nacos 中的配置：

```json
// namespace：myCustomNamespaceID
// group-id：CUSTOM_GROUP
// data-id：cloudalibaba-sentinel-service-flow-rules
// 配置格式：json
[
  {
    "resource": "/flowRule/testA",
    "limitApp": "default",
    "grade": 1,
    "count": 2,
    "strategy": 0,
    "controlBehavior": 0,
    "clusterMode": false
  }
]
```

#### 4、cloud-gateway-gateway9527

还是使用 cloud-gateway-gateway9527 模块，查看 Sentinel 搭配 Gateway 的效果：

- 在 Sentinel 控制台添加网关流控规则，查看限流效果。
- 自定义 WebExceptionHandler 实现类，用于处理 BlockException 异常。
- 自定义 BlockRequestHandler 实现类，用于自定义异常响应。

```xml
<dependecies>
    <!-- 引入sentinel进行服务降级熔断 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    </dependency>

    <!-- gateway网关整合sentinel进行限流降级 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
    </dependency>
</dependecies>
```

```yml
spring:
  cloud:
    sentinel:
      filter:
        enabled: false # false 表示不开启 URL 粒度的限流功能
      transport:
        dashboard: 192.168.190.134:8080 # TODO: 配置 Sentinel 控制台地址及端口号
        port: 8719 # Sentinel 控制台和客户端通信的端口号，不配置的话默认是 8719，如果被占用了会自动往上加 1 找一个未被占用的端口
        client-ip: 192.168.1.137 # TODO: 配置 Sentinel 客户端 IP（也就是本机 IP），不配置可能会识别成 Sentinel 控制台所在的 IP，导致错误
```

## 四、服务网关

### （一）Gateway

#### 1、cloud-gateway-gateway9527

服务网关功能：

- 自定义网关全局过滤器、局部过滤器。
- 与服务注册发现中心搭配，实现负载均衡。
- 与 Hystrix 搭配实现服务限流；与 Redis 搭配实现服务熔断。

```xml
<dependecies>
    <!-- Gateway 已包含 spring-web 依赖，不需要再引入相关依赖了-->
    <!-- SpringBoot整合Web组件 -->
    <!--        <dependency>-->
    <!--            <groupId>org.springframework.boot</groupId>-->
    <!--            <artifactId>spring-boot-starter-web</artifactId>-->
    <!--        </dependency>-->

    <!-- gateway 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>

    <!-- 服务注册中心的客户端 eureka-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- hystrix 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>

    <!-- redis 依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>
</dependecies>
```

```yml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway
  redis:
    host: 192.168.190.134
    port: 6379
    database: 0
  cloud:
    gateway:
      default-filters: # 默认过滤器
        - SetRequestHeader=X-Request-Red, Blue # 设置请求头
      routes:
        # http://localhost:9527/payment/lb?open=false
        - id: payment_routh # 路由的id，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001 # 目标服务地址
          predicates:
            - Path=/payment/lb # 路径相匹配的断言条件
            - Query=open,false # 参数相匹配的断言条件，参数名为open，值为false

        # http://localhost:9527/payment/lb
        - id: payment_routh2
          uri: lb://CLOUD-PAYMENT-SERVICE # lb://服务注册中心的微服务名称，这种方式开启负载均衡
          predicates:
            - Path=/payment/lb # 路径相匹配的断言条件
            - Cookie=name,[a-z]+
            - Cookie=value,[a-z]+
            - Header=GatewayNeed, \d+
            - Host=localhost:9527
            - Method=GET
            - Query=id,.+ # 匹配任意请求参数
          filters:
            - SetStatus=401 # 设置响应状态码
            - MyPartialFilter # 自定义过滤器：只有当请求中携带了 user-id 头部信息时，才能访问

        # http://localhost:9527/payment/timeout?timeout=true
        - id: payment_routh3
          uri: lb://CLOUD-PAYMENT-SERVICE
          predicates:
            - Path=/payment/**
            - Query=timeout,true
          filters:
            - name: Hystrix # 开启熔断
              args:
                name: fallbackcmd # 熔断后执行的命令
                fallbackUri: forward:/payment/fallback # 熔断后转发到的地址，必须能匹配 Path 断言

        # http://localhost:9527/payment/timeout?rateLimiter=true
        - id: payment_routh4
          uri: lb://CLOUD-PAYMENT-SERVICE
          predicates:
            - Path=/payment/get/**
            - Query=rateLimiter,true
          filters:
            - name: RequestRateLimiter # 进行服务限流
              args:
                redis-rate-limiter.replenishRate: 1 # 令牌每秒填充数量
                redis-rate-limiter.burstCapacity: 3 # 令牌桶总容量
                key-resolver: "#{@pathKeyResolver}" # 自定义的 MyPathKeyResolver 的 Bean 名称

hystrix:
  command:
    fallbackcmd: # 熔断后执行的命令
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 2000 #服务超时时间

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```

## 五、服务配置

### （一）Config

#### 1、cloud-config-center3344

服务配置中心服务端：

- @EnableConfigServer 开启配置中心服务端。
- 使用 github 作为配置仓库。
- 使用 Bus 服务总线，提供自动刷新配置的功能。
- 与 Eureka 服务注册发现中心搭配，方便客户端访问。

```xml
<dependecies>
    <!-- Config服务端 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- SpringCloud Bus 消息总线依赖，使用 Kafka 消息队列 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-kafka</artifactId>
    </dependency>

    <!-- Actuator 监控 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependecies>
```

```yml
server:
  port: 3344

eureka:
  client:
    register-with-eureka: true # 是否注册进 eureka 服务注册中心
    fetch-registry: true # 是否从 eureka 服务注册中心抓取已有的注册信息
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/

spring:
  application:
    name: cloud-config-center
  cloud:
    config:
      server:
        git:
          # 私有仓库需要配置账号密码
          # username: xxxx
          # password: xxxx
          uri: https://github.com/onezilin/StudyNotes-SpringCloudConfig.git
          # 当仓库下有子文件夹时，需要配置搜索路径，子文件夹中的同名文件会覆盖父文件夹中的同名文件
          search-paths:
            - foo
          # 默认请求的 git 分支名称，当请求中没有配置 label 时使用该值，例如：http://localhost:3344/config-dev.yml
          default-label: master
    stream:
      kafka:
        binder:
          brokers: 192.168.190.134:9092 # TODO: kafka 服务地址

# 暴露监控端点 否则 curl -X POST "http://localhost:3366/actuator/refresh" 不可使用
management:
  endpoints:
    web:
      exposure:
        include: "*" # 暴露所有端点
```

#### 2、cloud-config-client3355、cloud-config-client3366

服务配置中心客户端：

- 使用 bootstrap.yml 获取配置中心服务端的配置。
- 使用 Bus 服务总线，提供自动刷新配置的功能。
- 与 Eureka 服务注册发现中心搭配，访问服务端。

```xml
<dependecies>
    <!-- SpringCloud Config 客户端依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- SpringCloud Bus 消息总线依赖，使用 Kafka 消息队列 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-kafka</artifactId>
    </dependency>

    <!-- Actuator 监控 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependecies>
```

```yml
spring:
  application:
    name: cloud-config-client
  cloud:
    config:
      label: master # 分支名称
      name: config # 配置文件名称
      profile: dev # 读取后缀名称
      #      uri: http://localhost:3344  # 配置中心的地址，组合起来就是：http://localhost:3344/master/config-dev.yml
      discovery:
        service-id: CLOUD-CONFIG-CENTER # 从服务注册中心发现配置中心服务
        enabled: true # 开启服务注册中心
    stream:
      kafka:
        binder:
          brokers: 192.168.190.134:9092 # TODO: kafka 服务地址

# 暴露监控端点 否则 curl -X POST "http://localhost:3355/actuator/refresh" 不可使用
management:
  endpoints:
    web:
      exposure:
        include: "*" # 暴露所有端点

eureka:
  client:
    register-with-eureka: true # 是否注册进 eureka 服务注册中心
    fetch-registry: true # 是否从 eureka 服务注册中心抓取已有的注册信息
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```

### （二）Nacos

#### 1、cloudalibaba-consumer-nacos-client3377

使用 Nacos 作为服务配置中心。

- 测试 namespace、group、dataId 的作用。

```xml
<dependecies>
    <!-- Nacos 服务发现依赖 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        <version>2021.1</version>
    </dependency>

    <!-- Nacos 配置中心 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        <version>2021.1</version>
    </dependency>
</dependecies>
```

```yml
spring:
  application:
    name: nacos-config-client
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
        namespace: myCustomNamespaceID # 命名空间 namespace ID，在此 namespace 进行服务注册发现
        group: CUSTOM_GROUP # 组名
      config:
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
        file-extension: yml # 指定yaml文件
        namespace: myCustomNamespaceID # 命名空间 namespace ID，读取此 namespace 下的配置
        group: CUSTOM_GROUP # 组名
        extension-configs: # 读取同一 namespace 下其它 group 内指定 data-id 的配置
          - data-id: nacos-config-client-test.yml # 指定配置文件名
            group: DEFAULT_GROUP # 组名
            refresh: true # 是否自动刷新
```

## 六、分布式链路追踪

### （一）Sleuth

#### 1、cloud-consumer-order80、cloud-provider-payment8001、cloud-provider-payment8002

还是使用这几个模块，引入 Sleuth 依赖：

- 客户端进行请求后，在 Zipkin 界面中查看请求链路

```xml
<!-- 包含了sleuth + zipkin -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```

```yml
spring:
  zipkin:
    base-url: http://192.168.190.134:9411 # TODO: 配置 Zipkin Server 地址
  sleuth:
    sampler:
      probability: 1 # 采样率值介于 0 到 1 之间，1 则表示全部采集，0.1 则表示 10 个请求中只采集 1 一个
```

## 七、分布式事务

### （一）Seata

#### 1、seata-order-service2001、seata-account-service2002、seata-storage-service2003

- seata-order-service2001 订单服务，创建订单并修改状态。
- seata-account-service2002 扣减账户余额。
- seata-storage-service2003 扣减库存。

创建订单 -> 调用库存服务扣减库存 -> 调用账户服务扣减账户余额 -> 修改订单状态。

> 同一个 TM 中的不同 RM 可以使用不同的模式——XA、AT、TCC。

```xml
<dependencies>
    <!-- nacos 服务发现依赖 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>

    <!-- Nacos 配置中心 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        <version>2021.1</version>
    </dependency>

    <!-- openfeign 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!--seata-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
        <exclusions>
            <exclusion>
                <artifactId>seata-all</artifactId>
                <groupId>io.seata</groupId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>io.seata</groupId>
        <artifactId>seata-all</artifactId>
        <version>2.0.0</version>
    </dependency>

    <!-- mysql-druid -->
    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
    </dependency>
    <!-- https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.2.8</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-commons</artifactId>
    </dependency>
</dependencies>
```

bootstrap.yml：

```yml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
        namespace: seata # 命名空间 namespace ID，在此 namespace 进行服务注册发现
      config:
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
        file-extension: yml # 指定yaml文件
        namespace: seata # 命名空间 namespace ID，在此 namespace 进行服务注册发现
        group: SEATA_GROUP # 组名
```

application.yml：

```yml
server:
  port: 2001

spring:
  application:
    name: seata-order-service
  cloud:
    alibaba:
      seata:
        tx-service-group: seata_test_tx_group # TODO：连接到指定事务分组，对应 service.vgroupMapping.default_tx_group 中的 default_tx_group 名称
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://192.168.190.134:3306/seata_order?useUnicode=true&characterEncoding=utf-8&useSSL=false # TODO: 自定义 MySQL 地址
    username: root
    password: W110514

mybatis:
  mapper-locations: classpath:mapper/*.xml
  # TODO: 自定义
  type-aliases-package: com.atguigu.springcloud.entities

feign:
  hystrix:
    enabled: false
```

registry.conf：

```conf
registry {
  type = "nacos"

  nacos {
    application = "seata-server"
    serverAddr = "192.168.190.134:8848"
    group = "SEATA_GROUP"
    namespace = "seata"
    username = "nacos"
    password = "nacos"
    #contextPath = ""
    ##if use MSE Nacos with auth, mutex with username/password attribute
    #accessKey = ""
    #secretKey = ""
    ##if use Nacos naming meta-data for SLB service registry, specify nacos address pattern rules here
    #slbPattern = ""
  }
}

config {
  type = "nacos"

  nacos {
    serverAddr = "192.168.190.134:8848"
    namespace = "seata"
    group = "SEATA_GROUP"
    username = "nacos"
    password = "nacos"
    #contextPath = ""
    ##if use MSE Nacos with auth, mutex with username/password attribute
    #accessKey = ""
    #secretKey = ""
    dataId = "seataServer.properties"
  }
}
```

#### 2、seata-order-saga-service2004

用于测试 Seata 的 Saga 模式。

```xml
<dependencies>
    <!-- nacos 服务发现依赖 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>

    <!-- Nacos 配置中心 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        <version>2021.1</version>
    </dependency>

    <!-- openfeign 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!--seata-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
        <exclusions>
            <exclusion>
                <groupId>io.seata</groupId>
                <artifactId>seata-spring-boot-starter</artifactId>
            </exclusion>
            <exclusion>
                <groupId>io.seata</groupId>
                <artifactId>seata-all</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>io.seata</groupId>
        <artifactId>seata-spring-boot-starter</artifactId>
        <version>2.0.0</version>
        <exclusions>
            <exclusion>
                <groupId>io.seata</groupId>
                <artifactId>seata-all</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>io.seata</groupId>
        <artifactId>seata-all</artifactId>
        <version>2.0.0</version>
    </dependency>

    <!-- mysql-druid -->
    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
    </dependency>
    <!-- https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.2.8</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-commons</artifactId>
    </dependency>
</dependencies>
```

添加状态机配置类：

```java
@Configuration
public class StateMachineEngineConfig {

    @Resource
    private DataSource dataSource;

    @Value("${spring.application.name}")
    private String applicationId;

    @Value("${spring.cloud.alibaba.seata.tx-service-group}")
    private String txServiceGroup;

    /**
     * Description: 线程池
     */
    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("SAGA_ASYNC_EXEC_"); // 线程前缀名
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }

    /**
     * Description: 数据库状态机配置
     */
    @Bean
    public DbStateMachineConfig dbStateMachineConfig() {
        DbStateMachineConfig stateMachineConfig = new DbStateMachineConfig();
        stateMachineConfig.setDataSource(dataSource);
        stateMachineConfig.setResources(new String[]{"classpath:saga/*.json"});
        stateMachineConfig.setEnableAsync(true);
        stateMachineConfig.setThreadPoolExecutor(getThreadPoolExecutor());
        stateMachineConfig.setApplicationId(applicationId);
        stateMachineConfig.setTxServiceGroup(txServiceGroup);
        return stateMachineConfig;
    }

    @Bean
    public ProcessCtrlStateMachineEngine stateMachineEngine() {
        ProcessCtrlStateMachineEngine stateMachineEngine = new ProcessCtrlStateMachineEngine();
        stateMachineEngine.setStateMachineConfig(dbStateMachineConfig());
        return stateMachineEngine;
    }

    @Bean
    public StateMachineEngineHolder stateMachineEngineHolder() {
        StateMachineEngineHolder stateMachineEngineHolder = new StateMachineEngineHolder();
        stateMachineEngineHolder.setStateMachineEngine(stateMachineEngine());
        return stateMachineEngineHolder;
    }
}
```

bootstrap.yml：

```yml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
        namespace: seata # 命名空间 namespace ID，在此 namespace 进行服务注册发现
      config:
        server-addr: 192.168.190.134:8848 # TODO: Nacos Server 地址，多个可以用逗号隔开，这里我配置了 Nginx，所以这里写的是 Nginx 的地址
        file-extension: yml # 指定yaml文件
        namespace: seata # 命名空间 namespace ID，在此 namespace 进行服务注册发现
        group: SEATA_GROUP # 组名
```

application.yml：

```yml
server:
  port: 2004

spring:
  application:
    name: seata-order-saga-service
  cloud:
    alibaba:
      seata:
        tx-service-group: seata_test_tx_group # TODO：连接到指定事务分组，对应 service.vgroupMapping.default_tx_group 中的 default_tx_group 名称
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://192.168.190.134:3306/seata_order?useUnicode=true&characterEncoding=utf-8&useSSL=false # TODO: 自定义 MySQL 地址
    username: root
    password: W110514

mybatis:
  mapper-locations: classpath:mapper/*.xml
  # TODO: 自定义
  type-aliases-package: com.atguigu.springcloud.entities

feign:
  hystrix:
    enabled: false

seata:
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: 192.168.190.134:8848
      group: SEATA_GROUP
      namespace: seata
      username: nacos
      password: nacos
  config:
    type: nacos
    nacos:
      server-addr: 192.168.190.134:8848
      group: SEATA_GROUP
      namespace: seata
      username: nacos
      password: nacos
      data-id: seataServer.properties
```

> 在这里没有使用 registry.conf 配置文件，使用 seata-spring-boot-starter，通过 yml 方式配置。
