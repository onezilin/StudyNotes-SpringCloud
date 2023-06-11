package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (Payment)表数据库访问层
 *
 * @author onezilin
 * @since 2023-06-04 19:15:17
 */
@Mapper
public interface PaymentDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Payment queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param payment  查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Payment> queryAllByLimit(Payment payment, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param payment 查询条件
     * @return 总行数
     */
    long count(Payment payment);

    /**
     * 新增数据
     *
     * @param payment 实例对象
     * @return 影响行数
     */
    int insert(Payment payment);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Payment> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Payment> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Payment> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Payment> entities);

    /**
     * 修改数据
     *
     * @param payment 实例对象
     * @return 影响行数
     */
    int update(Payment payment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

