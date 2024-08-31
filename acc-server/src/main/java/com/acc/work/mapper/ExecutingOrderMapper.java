package com.acc.work.mapper;

import com.acc.dto.ExecutingOrderDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExecutingOrderMapper {

    @Select("SELECT * FROM executing_order WHERE id = #{id}")
    ExecutingOrderDto getById(int id);

    @Select("SELECT * FROM executing_order WHERE order_id = #{orderId}")
    List<ExecutingOrderDto> getByOrderId(int orderId);


    @Insert("INSERT INTO executing_order (id, order_id, executing_procedure_id, executing, update_time) " +
            "VALUES (#{id}, #{orderId}, #{executingProcedureId}, #{executing}, #{update_time})")
    void insertExecutingOrder(ExecutingOrderDto executingOrderDto);

    @Update("UPDATE executing_order SET " +
            "order_id = #{orderId}, " +
            "executing_procedure_id = #{executingProcedureId}, " +
            "executing = #{executing}, " +
            "update_time = #{update_time} " +
            "WHERE id = #{id}")
    void updateExecutingOrder(ExecutingOrderDto executingOrderDto);

    @Delete("DELETE FROM executing_order WHERE id = #{id}")
    void deleteExecutingOrder(int id);

    @Delete("DELETE FROM executing_order WHERE order_id = #{orderId}")
    void deleteExecutingOrderByOrderId(int orderId);

    @Select("SELECT * FROM executing_order")
    List<ExecutingOrderDto> findAllExecutingOrders();

}
