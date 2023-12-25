package com.atguigu.springcloud.tcc.impl;

import com.atguigu.springcloud.dao.TOrderDao;
import com.atguigu.springcloud.entities.TOrder;
import com.atguigu.springcloud.tcc.CreateOrderTccAction;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: TCC 模式下的订单接口实现类，实现 createOrder 创建订单功能
 */
@Component("createOrderTccAction")
@Slf4j
public class CreateOrderTccActionImpl implements CreateOrderTccAction {

    @Resource
    private TOrderDao tOrderDao;

    @Override
    public void prepareCreateOrder(@BusinessActionContextParameter(paramName = "order") TOrder order,
                                   BusinessActionContext actionContext) {
        this.tOrderDao.createOrder(order);
        log.info("------->创建订单结束：TCC 一阶段 try 方法执行成功");
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        TOrder order = actionContext.getActionContext("order", TOrder.class);
        // 修改订单状态为已完结
        assert order != null;
        this.tOrderDao.updateStatus(order.getId(), 1);
        log.info("------->创建订单结束：TCC 二阶段 commit 方法执行成功");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        TOrder order = actionContext.getActionContext("order", TOrder.class);
        // 修改订单状态为已取消
        assert order != null;
        this.tOrderDao.updateStatus(order.getId(), 0);
        log.info("------->创建订单结束：TCC 二阶段 rollback 方法执行成功");
        return true;
    }
}
