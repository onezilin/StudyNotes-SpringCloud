package com.atguigu.springcloud.config;

import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description: Saga 状态机配置类
 */
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
