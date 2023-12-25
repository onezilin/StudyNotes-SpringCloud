package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.TOrder;

/**
 * Description: 业务接口
 */
public interface BusinessService {

    /**
     * Description: 下单接口
     *
     * @param order 订单
     */
    void purchaseByAT(TOrder order);

    /**
     * Description: 下单接口
     *
     * @param order 订单
     */
    void purchaseByTCC(TOrder order);
}
