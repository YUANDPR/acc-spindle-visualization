package com.acc.mapper;

import com.acc.core.entity.WorkProcedure;

import java.util.List;


/**
 * 工序管理Mapper接口
 */
public interface WorkProcedureMapper {
    /**
     * 查询工序管理
     *
     * @param id 工序管理主键
     * @return 工序管理
     */
    WorkProcedure selectWorkProcedureById(Long id);

    /**
     * 查询工序管理列表
     *
     * @param workProcedure 工序管理
     * @return 工序管理集合
     */
    List<WorkProcedure> selectWorkProcedureList(WorkProcedure workProcedure);

    /**
     * 新增工序管理
     *
     * @param workProcedure 工序管理
     * @return 结果
     */
    int insertWorkProcedure(WorkProcedure workProcedure);

    /**
     * 修改工序管理
     *
     * @param workProcedure 工序管理
     * @return 结果
     */
    int updateWorkProcedure(WorkProcedure workProcedure);

    /**
     * 删除工序管理
     *
     * @param id 工序管理主键
     * @return 结果
     */
    int deleteWorkProcedureById(Long id);

    /**
     * 批量删除工序管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteWorkProcedureByIds(String[] ids);
}
