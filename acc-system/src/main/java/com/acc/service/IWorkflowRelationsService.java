package com.acc.service;

import com.acc.core.entity.WorkflowRelations;

import java.util.List;


/**
 * 工作组间关系一维化Service接口
 *
 * @author ruoyi
 * @date 2025-04-28
 */
public interface IWorkflowRelationsService {
    /**
     * 查询工作组间关系一维化
     *
     * @param id 工作组间关系一维化主键
     * @return 工作组间关系一维化
     */
    public WorkflowRelations selectWorkflowRelationsById(Long id);

    /**
     * 查询工作组间关系一维化列表
     *
     * @param workflowRelations 工作组间关系一维化
     * @return 工作组间关系一维化集合
     */
    public List<WorkflowRelations> selectWorkflowRelationsList(WorkflowRelations workflowRelations);

    /**
     * 新增工作组间关系一维化
     *
     * @param workflowRelations 工作组间关系一维化
     * @return 结果
     */
    public int insertWorkflowRelations(WorkflowRelations workflowRelations);

    /**
     * 修改工作组间关系一维化
     *
     * @param workflowRelations 工作组间关系一维化
     * @return 结果
     */
    public int updateWorkflowRelations(WorkflowRelations workflowRelations);

    /**
     * 批量删除工作组间关系一维化
     *
     * @param ids 需要删除的工作组间关系一维化主键集合
     * @return 结果
     */
    public int deleteWorkflowRelationsByIds(String ids);

    /**
     * 删除工作组间关系一维化信息
     *
     * @param id 工作组间关系一维化主键
     * @return 结果
     */
    public int deleteWorkflowRelationsById(Long id);
}
