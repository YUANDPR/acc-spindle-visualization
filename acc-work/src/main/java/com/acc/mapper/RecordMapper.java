package com.acc.mapper;


import com.acc.core.entity.WorkRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecordMapper {


    // 插入工作记录
    @Insert("INSERT INTO work_record(id, order_id, procedure_id, operator, time, start_or_finish) VALUES(#{id}, #{orderId}, #{procedureId}, #{operator}, #{time}, #{startOrFinish})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertWorkRecord(WorkRecord workRecord);

    // 按 orderId 查询工作记录
    @Select("SELECT * FROM work_record WHERE order_id = #{orderId}")
    List<WorkRecord> findWorkRecordsByOrderId(int orderId);

    // 按 operator 查询工作记录
    @Select("SELECT * FROM work_record WHERE operator = #{operator}")
    List<WorkRecord> findWorkRecordsByOperator(String operator);

    // 按 orderId 删除工作记录
    @Delete("DELETE FROM work_record WHERE order_id = #{orderId}")
    void deleteWorkRecordsByOrderId(int orderId);

    // 按 id 删除工作记录
    @Delete("DELETE FROM work_record WHERE id = #{id}")
    void deleteWorkRecordById(int id);

    // 查询所有工作记录
    @Select("SELECT * FROM work_record")
    List<WorkRecord> findAllWorkRecords();
}
