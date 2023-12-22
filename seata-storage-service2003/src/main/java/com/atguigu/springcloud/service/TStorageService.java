package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.TStorage;

/**
 * 库存(TStorage)表服务接口
 *
 * @author onezilin
 * @since 2023-12-22 10:56:17
 */
public interface TStorageService {

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
    void decreaseStorage(Long id, Integer count);

}
