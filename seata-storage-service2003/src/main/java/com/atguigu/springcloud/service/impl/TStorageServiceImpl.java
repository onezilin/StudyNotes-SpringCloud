package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.dao.TStorageDao;
import com.atguigu.springcloud.entities.TStorage;
import com.atguigu.springcloud.service.TStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 库存(TStorage)表服务实现类
 *
 * @author onezilin
 * @since 2023-12-22 10:56:18
 */
@Service("tStorageService")
@Slf4j
public class TStorageServiceImpl implements TStorageService {
    @Resource
    private TStorageDao tStorageDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TStorage queryById(Long id) {
        return this.tStorageDao.queryById(id);
    }


    /**
     * Description: 扣减库存
     *
     * @param id    库存 id
     * @param count 扣减数量
     */
    @Override
    public void decreaseStorage(Long id, Integer count) {
        log.info("------->开始扣减库存");
        this.tStorageDao.decreaseStorage(id, count);
        log.info("------->扣减库存结束");
    }
}
