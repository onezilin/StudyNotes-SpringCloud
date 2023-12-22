package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entities.TAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 账户表(TAccount)表数据库访问层
 *
 * @author onezilin
 * @since 2023-12-22 10:44:36
 */
@Mapper
public interface TAccountDao {

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
     * @param id      用户id
     * @param used    已用余额
     * @param residue 剩余可用额度
     */
    void decreaseMoney(@Param("id") Long id, @Param("used") Double used, @Param("residue") Double residue);

}

