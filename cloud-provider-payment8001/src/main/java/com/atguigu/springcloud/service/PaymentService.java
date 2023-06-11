package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (Payment)表服务接口
 *
 * @author onezilin
 * @since 2023-06-04 19:20:51
 */
public interface PaymentService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Payment queryById(Long id);

    /**
     * 分页查询
     *
     * @param payment     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Payment> queryByPage(Payment payment, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param payment 实例对象
     * @return 实例对象
     */
    Payment insert(Payment payment);

    /**
     * 修改数据
     *
     * @param payment 实例对象
     * @return 实例对象
     */
    Payment update(Payment payment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
