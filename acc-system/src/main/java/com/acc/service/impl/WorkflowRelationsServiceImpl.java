package com.acc.service.impl;

import com.acc.core.entity.WorkflowRelations;
import com.acc.core.text.Convert;
import com.acc.mapper.WorkflowRelationsMapper;
import com.acc.service.IWorkflowRelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 工作组间关系一维化Service业务层处理
 *
 * @author ruoyi
 * @date 2025-04-28
 */
@Service
public class WorkflowRelationsServiceImpl implements IWorkflowRelationsService {
    @Autowired
    private WorkflowRelationsMapper workflowRelationsMapper;

    /**
     * 查询工作组间关系一维化
     *
     * @param id 工作组间关系一维化主键
     * @return 工作组间关系一维化
     */
    @Override
    public WorkflowRelations selectWorkflowRelationsById(Long id) {
        return workflowRelationsMapper.selectWorkflowRelationsById(id);
    }

    /**
     * 查询工作组间关系一维化列表
     *
     * @param workflowRelations 工作组间关系一维化
     * @return 工作组间关系一维化
     */
    @Override
    public List<WorkflowRelations> selectWorkflowRelationsList(WorkflowRelations workflowRelations) {
        return workflowRelationsMapper.selectWorkflowRelationsList(workflowRelations);
    }

    /**
     * 新增工作组间关系一维化
     *
     * @param workflowRelations 工作组间关系一维化
     * @return 结果
     */
    @Override
    public int insertWorkflowRelations(WorkflowRelations workflowRelations) {
        return workflowRelationsMapper.insertWorkflowRelations(workflowRelations);
    }

    /**
     * 修改工作组间关系一维化
     *
     * @param workflowRelations 工作组间关系一维化
     * @return 结果
     */
    @Override
    public int updateWorkflowRelations(WorkflowRelations workflowRelations) {
        return workflowRelationsMapper.updateWorkflowRelations(workflowRelations);
    }

    /**
     * 批量删除工作组间关系一维化
     *
     * @param ids 需要删除的工作组间关系一维化主键
     * @return 结果
     */
    @Override
    public int deleteWorkflowRelationsByIds(String ids) {
        return workflowRelationsMapper.deleteWorkflowRelationsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除工作组间关系一维化信息
     *
     * @param id 工作组间关系一维化主键
     * @return 结果
     */
    @Override
    public int deleteWorkflowRelationsById(Long id) {
        return workflowRelationsMapper.deleteWorkflowRelationsById(id);
    }
}
