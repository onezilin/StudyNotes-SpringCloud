package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.entities.TOrder;
import com.atguigu.springcloud.service.BusinessService;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateMachineInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 业务接口实现类
 */
@Service("businessService")
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    // 不使用 tOrderService 调用，改为状态机启动
    // @Resource
    // private TOrderService tOrderService;
    @Resource
    private StateMachineEngine stateMachineEngine;

    /**
     * Description:
     * <p>
     * 1、测试正常情况时，状态机的执行流程
     * 2、在{@link TOrderServiceImpl#createOrder} 添加 RuntimeException 或将返回值改为 false，查看状态机的执行流程，查看补偿接口是否调用
     *
     * @param order 订单
     */
    @Override
    public void purchaseBySaga(TOrder order) {
        Map<String, Object> startParams = new HashMap<>();
        String businessKey = String.valueOf(System.currentTimeMillis());
        startParams.put("businessKey", businessKey);
        startParams.put("order", order);

        StateMachineInstance instance = stateMachineEngine.startWithBusinessKey("purchaseBySaga", null, businessKey,
                startParams);

        if (ExecutionStatus.SU.equals(instance.getStatus())) {
            log.info("执行成功，返回结果：{}", instance.getEndParams());
        } else {
            log.info("执行失败，返回结果：{}", instance.getEndParams());
        }
    }

}
