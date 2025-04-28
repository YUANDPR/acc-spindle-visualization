package com.acc.service;

import com.acc.core.entity.WorkorderDistanceSummary;

import java.util.List;


/**
 * 工单工艺流程流转总距离汇总Service接口
 *
 * @author ruoyi
 * @date 2025-04-28
 */
public interface IWorkorderDistanceSummaryService {
    /**
     * 查询工单工艺流程流转总距离汇总
     *
     * @param id 工单工艺流程流转总距离汇总主键
     * @return 工单工艺流程流转总距离汇总
     */
    public WorkorderDistanceSummary selectWorkorderDistanceSummaryById(Long id);

    /**
     * 查询工单工艺流程流转总距离汇总列表
     *
     * @param workorderDistanceSummary 工单工艺流程流转总距离汇总
     * @return 工单工艺流程流转总距离汇总集合
     */
    public List<WorkorderDistanceSummary> selectWorkorderDistanceSummaryList(WorkorderDistanceSummary workorderDistanceSummary);

    /**
     * 新增工单工艺流程流转总距离汇总
     *
     * @param workorderDistanceSummary 工单工艺流程流转总距离汇总
     * @return 结果
     */
    public int insertWorkorderDistanceSummary(WorkorderDistanceSummary workorderDistanceSummary);

    /**
     * 修改工单工艺流程流转总距离汇总
     *
     * @param workorderDistanceSummary 工单工艺流程流转总距离汇总
     * @return 结果
     */
    public int updateWorkorderDistanceSummary(WorkorderDistanceSummary workorderDistanceSummary);

    /**
     * 批量删除工单工艺流程流转总距离汇总
     *
     * @param ids 需要删除的工单工艺流程流转总距离汇总主键集合
     * @return 结果
     */
    public int deleteWorkorderDistanceSummaryByIds(String ids);

    /**
     * 删除工单工艺流程流转总距离汇总信息
     *
     * @param id 工单工艺流程流转总距离汇总主键
     * @return 结果
     */
    public int deleteWorkorderDistanceSummaryById(Long id);

    /**
     * 更新总距离表
     */
    int update();

    /**
     * 获取总距离
     */
    int getAccount();

    /**
     * 获取所有信息
     */
    List<WorkorderDistanceSummary> getAll();
}
