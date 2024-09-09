package com.acc.mapper;

import com.acc.core.entity.WorkProcedure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProcedureMapper {

    @Select("SELECT * FROM work_procedure WHERE id = #{id}")
    WorkProcedure getById(Long id);

    @Select("SELECT * FROM work_procedure WHERE material_id = #{materialId} ORDER BY operation_id")
    List<WorkProcedure> findByMaterialIdOrderByOperationId(String materialId);

    void insertBatch(List<WorkProcedure> workProcedures);
    // 可以在这里添加其他方法，例如update, delete, insert等
}
