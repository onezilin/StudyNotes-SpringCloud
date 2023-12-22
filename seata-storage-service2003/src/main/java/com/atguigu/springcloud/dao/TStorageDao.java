package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entities.TStorage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存(TStorage)表数据库访问层
 *
 * @author onezilin
 * @since 2023-12-22 10:56:17
 */
@Mapper
public interface TStorageDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TStorage queryById(Long id);

    /**
     * Description: 扣减库存
     *
     * @param id    库存 id
     * @param count 扣减数量
     */
    void decreaseStorage(@Param("id") Long id, @Param("count") Integer count);
}

