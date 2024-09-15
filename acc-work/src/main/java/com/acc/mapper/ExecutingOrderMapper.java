package com.acc.mapper;

import com.acc.core.dto.ExecutingOrderDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 执行中订单
 */
@Mapper
public interface ExecutingOrderMapper {
    /**
     * 通过ID获取执行订单信息
     *
     * @param id 执行订单ID
     * @return 执行订单数据传输对象(ExecutingOrderDto)
     */
    @Select("SELECT * FROM work_executing_order WHERE id = #{id}")
    ExecutingOrderDto getById(int id);

    /**
     * 通过订单ID获取执行订单列表
     *
     * @param orderId 订单ID
     * @return 执行订单数据传输对象列表(List < ExecutingOrderDto >)
     */
    @Select("SELECT * FROM work_executing_order WHERE order_id = #{orderId}")
    List<ExecutingOrderDto> getByOrderId(int orderId);

    /**
     * 插入执行订单信息
     *
     * @param executingOrderDto 执行订单数据传输对象
     */
    @Insert("INSERT INTO work_executing_order (id, order_id, executing_procedure_id, executing, update_time) " +
            "VALUES (#{id}, #{orderId}, #{executingProcedureId}, #{executing}, #{updateTime})")
    void insertExecutingOrder(ExecutingOrderDto executingOrderDto);

    /**
     * 更新执行订单信息
     *
     * @param executingOrderDto 执行订单数据传输对象
     */
    @Update("UPDATE work_executing_order SET " +
            "order_id = #{orderId}, " +
            "executing_procedure_id = #{executingProcedureId}, " +
            "executing = #{executing}, " +
            "update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void updateExecutingOrder(ExecutingOrderDto executingOrderDto);

    /**
     * 通过ID删除执行订单信息
     *
     * @param id 执行订单ID
     */
    @Delete("DELETE FROM work_executing_order WHERE id = #{id}")
    void deleteExecutingOrder(int id);

    /**
     * 通过订单ID删除执行订单信息
     *
     * @param orderId 订单ID
     */
    @Delete("DELETE FROM work_executing_order WHERE order_id = #{orderId}")
    void deleteExecutingOrderByOrderId(int orderId);

    /**
     * 获取所有执行订单信息
     *
     * @return 所有执行订单的数据传输对象列表(List < ExecutingOrderDto >)
     */
    @Select("SELECT * FROM work_executing_order")
    List<ExecutingOrderDto> findAllExecutingOrders();

}

