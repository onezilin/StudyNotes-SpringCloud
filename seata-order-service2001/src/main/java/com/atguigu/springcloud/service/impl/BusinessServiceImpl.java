package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.entities.TOrder;
import com.atguigu.springcloud.service.BusinessService;
import com.atguigu.springcloud.service.TAccountService;
import com.atguigu.springcloud.service.TOrderService;
import com.atguigu.springcloud.service.TStorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Description: 业务接口实现类
 */
@Service("businessService")
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private TOrderService tOrderService;

    @Resource
    private TStorageService tStorageService;

    @Resource
    private TAccountService tAccountService;

    /**
     * Description: 下单接口，创建订单 -> 调用库存服务扣减库存 -> 调用账户服务扣减账户余额 -> 修改订单状态
     * <p>
     * 测试步骤：
     * 1、测试没有 @GlobalTransactional 时，测试**服务正常执行和抛异常**的情况，查看数据库数据的一致性
     * 2、测试有 @GlobalTransactional 时，测试**服务正常执行和抛异常**的情况，查看数据库数据的一致性
     *
     * @param order 订单
     */
    @Override
    @GlobalTransactional
    public void purchaseByAT(TOrder order) {
        log.info("------->下单开始");

        // 1. 创建订单
        log.info("------->开始新建订单");
        tOrderService.createOrder(order);

        // 2. 调用库存服务扣减库存
        log.info("------->订单微服务开始调用库存，做扣减Count");
        tStorageService.decreaseStorage(order.getProductId(), order.getCount());

        // 3. 调用账户服务扣减账户余额
        log.info("------->订单微服务开始调用账户，做扣减Money");
        tAccountService.decreaseMoney(order.getUserId(), BigDecimal.valueOf(order.getMoney()));

        // 4. 修改订单状态
        log.info("------->修改订单状态开始");
        tOrderService.updateStatus(order.getId(), 1);

        log.info("------->下单结束");

        // throw new RuntimeException("模拟异常");
    }

    /**
     * Description: 下单接口，创建订单 -> 调用库存服务扣减库存 -> 调用账户服务扣减账户余额 -> 修改订单状态
     * <p>
     * 测试步骤：
     * 1、测试没有 @GlobalTransactional 时，测试**服务正常执行和抛异常**的情况，查看数据库数据的一致性
     * 2、测试有 @GlobalTransactional 时，测试**服务正常执行和抛异常**的情况，查看数据库数据的一致性
     *
     * @param order 订单
     */
    @Override
    @GlobalTransactional
    public void purchaseByTCC(TOrder order) {
        log.info("------->下单开始");

        // 1. 创建订单
        log.info("------->开始新建订单");
        tOrderService.tccCreateOrder(order);

        // 2. 调用库存服务扣减库存
        log.info("------->订单微服务开始调用库存，做扣减Count");
        tStorageService.decreaseStorage(order.getProductId(), order.getCount());

        // 3. 调用账户服务扣减账户余额
        log.info("------->订单微服务开始调用账户，做扣减Money");
        tAccountService.decreaseMoney(order.getUserId(), BigDecimal.valueOf(order.getMoney()));

        log.info("------->下单结束");

        // throw new RuntimeException("模拟异常");
    }
}
