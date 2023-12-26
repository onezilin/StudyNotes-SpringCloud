package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entities.TOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单表(TOrder)表数据库访问层
 *
 * @author onezilin
 * @since 2023-12-22 10:38:38
 */
@Mapper
public interface TOrderDao {

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
    void createOrder(TOrder order);

    /**
     * Description: 修改订单状态
     *
     * @param id     订单 id
     * @param status 订单状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
}

