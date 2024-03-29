package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.TOrder;

/**
 * 订单表(TOrder)表服务接口
 *
 * @author onezilin
 * @since 2023-12-22 10:38:42
 */
public interface TOrderService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TOrder queryById(Long id);

    /**
     * Description: 创建订单
     *
     * @param order 订单
     */
    boolean createOrder(TOrder order);

    /**
     * Description: Saga 模式下，创建订单的补偿接口
     *
     * @param order 订单
     */
    void createOrderCompensation(TOrder order);

    /**
     * Description: 修改订单状态
     *
     * @param id     订单 id
     * @param status 订单状态
     */
    void updateStatus(Long id, Integer status);
}
