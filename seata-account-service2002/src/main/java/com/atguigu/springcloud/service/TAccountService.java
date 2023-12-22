package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.TAccount;

import java.math.BigDecimal;

/**
 * 账户表(TAccount)表服务接口
 *
 * @author onezilin
 * @since 2023-12-22 10:44:38
 */
public interface TAccountService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TAccount queryById(Long id);

    /**
     * 扣减账户余额
     *
     * @param id    用户id
     * @param money 扣减金额
     */
    void decreaseMoney(Long id, BigDecimal money);
}
