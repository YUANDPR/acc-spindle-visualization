package com.acc.mapper;

import com.acc.core.entity.WorkOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM work_order WHERE id = #{id}")
    WorkOrder getById(int id);


    @Insert("INSERT INTO work_order (id, order_id, material_id, description, order_quantity, start_time, finish_time) " +
            "VALUES (#{id}, #{orderId}, #{materialId}, #{description}, #{orderQuantity}, #{startTime}, #{finishTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertWorkOrder(WorkOrder workOrder);

    void insertBatch(List<WorkOrder> workOrders);

    @Update("UPDATE work_order SET " +
            "order_id = #{orderId}, " +
            "material_id = #{materialId}, " +
            "description = #{description}, " +
            "order_quantity = #{orderQuantity}, " +
            "start_time = #{startTime}, " +
            "finish_time = #{finishTime} " +
            "WHERE id = #{id}")
    void updateWorkOrder(WorkOrder workOrder);

    @Delete("DELETE FROM work_order WHERE id = #{id}")
    void deleteWorkOrder(int id);

    @Delete("DELETE FROM work_order WHERE order_id = #{orderId}")
    void deleteWorkOrdersByOrderId(Integer orderId);

    @Select("SELECT id FROM work_order WHERE order_id = #{orderId}")
    List<Integer> findWorkOrderIdsByOrderId(int orderId);
}
