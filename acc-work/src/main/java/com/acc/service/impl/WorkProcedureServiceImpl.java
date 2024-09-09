package com.acc.service.impl;

import com.acc.core.entity.WorkProcedure;
import com.acc.core.text.Convert;
import com.acc.mapper.WorkProcedureMapper;
import com.acc.service.WorkProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工序管理Service业务层处理
 */
@Service
public class WorkProcedureServiceImpl implements WorkProcedureService {
    @Autowired
    private WorkProcedureMapper workProcedureMapper;

    /**
     * 查询工序管理
     *
     * @param id 工序管理主键
     * @return 工序管理
     */
    @Override
    public WorkProcedure selectWorkProcedureById(Long id) {
        return workProcedureMapper.selectWorkProcedureById(id);
    }

    /**
     * 查询工序管理列表
     *
     * @param workProcedure 工序管理
     * @return 工序管理
     */
    @Override
    public List<WorkProcedure> selectWorkProcedureList(WorkProcedure workProcedure) {
        return workProcedureMapper.selectWorkProcedureList(workProcedure);
    }

    /**
     * 新增工序管理
     *
     * @param workProcedure 工序管理
     * @return 结果
     */
    @Override
    public int insertWorkProcedure(WorkProcedure workProcedure) {
            return workProcedureMapper.insertWorkProcedure(workProcedure);
    }

    /**
     * 修改工序管理
     *
     * @param workProcedure 工序管理
     * @return 结果
     */
    @Override
    public int updateWorkProcedure(WorkProcedure workProcedure) {
        return workProcedureMapper.updateWorkProcedure(workProcedure);
    }

    /**
     * 批量删除工序管理
     *
     * @param ids 需要删除的工序管理主键
     * @return 结果
     */
    @Override
    public int deleteWorkProcedureByIds(String ids) {
        return workProcedureMapper.deleteWorkProcedureByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除工序管理信息
     *
     * @param id 工序管理主键
     * @return 结果
     */
    @Override
    public int deleteWorkProcedureById(Long id) {
        return workProcedureMapper.deleteWorkProcedureById(id);
    }
}
