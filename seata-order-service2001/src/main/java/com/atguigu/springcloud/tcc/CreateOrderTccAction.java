package com.atguigu.springcloud.tcc;

import com.atguigu.springcloud.entities.TOrder;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * Description: TCC 模式下的订单接口，用于实现分布式事务
 * <p>
 * 为 createOrder（对应 TOrderService.createOrder() 方法）的动作添加 try、confirm、cancel 方法
 * <p>
 * 固定格式：
 * * @LocalTCC 注解添加在接口上，表示该接口是一个 TCC 模式下的接口。
 * * @TwoPhaseBusinessAction 注解 try 方法，其中 name 属性为当前 tcc 方法的 bean 名称（全局唯一），commitMethod 属性为 confirm
 * 方法的名称，rollbackMethod 属性为 cancel 方法的名称。
 * * @BusinessActionContextParameter 注解可以将参数传递到二阶段（commitMethod/rollbackMethod）的方法。
 * * BusinessActionContext 便是指TCC事务上下文。
 */
@LocalTCC
public interface CreateOrderTccAction {

    /**
     * Description: 一阶段方法，用于执行业务操作（try）
     *
     * @param actionContext TCC 事务上下文
     * @param order         订单
     * @return void
     */
    @TwoPhaseBusinessAction(name = "createOrderTccAction.prepareCreateOrder", commitMethod = "commit",
            rollbackMethod = "rollback")
    void prepareCreateOrder(TOrder order, BusinessActionContext actionContext);

    /**
     * Description: 二阶段方法，用于提交事务（commit）
     *
     * @param actionContext TCC 事务上下文
     * @return boolean
     */
    boolean commit(BusinessActionContext actionContext);

    /**
     * Description: 二阶段方法，用于回滚事务（rollback）
     *
     * @param actionContext TCC 事务上下文
     * @return boolean
     */
    boolean rollback(BusinessActionContext actionContext);
}
