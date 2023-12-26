package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.dao.TOrderDao;
import com.atguigu.springcloud.entities.TOrder;
import com.atguigu.springcloud.service.TOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单表(TOrder)表服务实现类
 *
 * @author onezilin
 * @since 2023-12-22 10:38:42
 */
@Service("tOrderService")
@Slf4j
public class TOrderServiceImpl implements TOrderService {
    @Resource
    private TOrderDao tOrderDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TOrder queryById(Long id) {
        return this.tOrderDao.queryById(id);
    }

    /**
     * Description: 创建订单
     *
     * @param order 订单
     */
    @Override
    public boolean createOrder(TOrder order) {
        log.info("------->开始新建订单");
        this.tOrderDao.createOrder(order);
        log.info("------->新建订单结束");
        // throw new RuntimeException("模拟异常");
        return true;
    }

    /**
     * Description: Saga 模式下，创建订单的补偿接口
     *
     * @param order 订单
     */
    @Override
    public void createOrderCompensation(TOrder order) {
        log.info("测试补偿接口是否调用");
    }

    /**
     * Description: 修改订单状态
     *
     * @param id     订单 id
     * @param status 订单状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        log.info("------->开始修改订单状态");
        this.tOrderDao.updateStatus(id, status);
        log.info("------->修改订单状态结束");
    }
}
