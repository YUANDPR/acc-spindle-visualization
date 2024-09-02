package com.acc.mapper;

import com.acc.core.dto.ExecutingOrderDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExecutingOrderMapper {

    @Select("SELECT * FROM work_executing_order WHERE id = #{id}")
    ExecutingOrderDto getById(int id);

    @Select("SELECT * FROM work_executing_order WHERE order_id = #{orderId}")
    List<ExecutingOrderDto> getByOrderId(int orderId);


    @Insert("INSERT INTO work_executing_order (id, order_id, executing_procedure_id, executing, update_time) " +
            "VALUES (#{id}, #{orderId}, #{executingProcedureId}, #{executing}, #{update_time})")
    void insertExecutingOrder(ExecutingOrderDto executingOrderDto);

    @Update("UPDATE work_executing_order SET " +
            "order_id = #{orderId}, " +
            "executing_procedure_id = #{executingProcedureId}, " +
            "executing = #{executing}, " +
            "update_time = #{update_time} " +
            "WHERE id = #{id}")
    void updateExecutingOrder(ExecutingOrderDto executingOrderDto);

    @Delete("DELETE FROM work_executing_order WHERE id = #{id}")
    void deleteExecutingOrder(int id);

    @Delete("DELETE FROM work_executing_order WHERE order_id = #{orderId}")
    void deleteExecutingOrderByOrderId(int orderId);

    @Select("SELECT * FROM work_executing_order")
    List<ExecutingOrderDto> findAllExecutingOrders();

}
