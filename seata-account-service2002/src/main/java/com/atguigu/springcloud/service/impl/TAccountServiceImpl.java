package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.dao.TAccountDao;
import com.atguigu.springcloud.entities.TAccount;
import com.atguigu.springcloud.service.TAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 账户表(TAccount)表服务实现类
 *
 * @author onezilin
 * @since 2023-12-22 10:44:39
 */
@Service("tAccountService")
@Slf4j
public class TAccountServiceImpl implements TAccountService {
    @Resource
    private TAccountDao tAccountDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TAccount queryById(Long id) {
        return this.tAccountDao.queryById(id);
    }

    /**
     * 扣减账户余额，@Transactional 注解用于开启本地事务，如果要保证方法的事务就需要添加
     *
     * @param id    用户id
     * @param money 扣减金额
     */
    @Override
    @Transactional
    public void decreaseMoney(Long id, BigDecimal money) {
        TAccount account = this.tAccountDao.queryById(id);
        Double residue = account.getResidue();
        if (residue < money.doubleValue()) {
            throw new RuntimeException("账户余额不足");
        } else {
            BigDecimal residueBigDecimal = new BigDecimal(residue);
            account.setResidue(residueBigDecimal.subtract(money).doubleValue());
            BigDecimal usedBigDecimal = BigDecimal.valueOf(account.getUsed());
            account.setUsed(usedBigDecimal.add(money).doubleValue());
        }

        log.info("------->开始扣减账户余额");
        this.tAccountDao.decreaseMoney(id, account.getUsed(), account.getResidue());
        log.info("------->扣减账户余额成功");
    }
}
