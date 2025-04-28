package com.acc.service.impl;

import com.acc.core.entity.ContrastTable;
import com.acc.core.entity.WorkorderDistanceSummary;
import com.acc.core.text.Convert;
import com.acc.mapper.ContrastTableMapper;
import com.acc.mapper.WorkorderDistanceSummaryMapper;
import com.acc.service.IWorkorderDistanceSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 工单工艺流程流转总距离汇总Service业务层处理
 *
 * @author ruoyi
 * @date 2025-04-28
 */
@Service
public class WorkorderDistanceSummaryServiceImpl implements IWorkorderDistanceSummaryService {
    @Autowired
    private WorkorderDistanceSummaryMapper workorderDistanceSummaryMapper;

    @Autowired
    private ContrastTableMapper contrastTableMapper;

    /**
     * 查询工单工艺流程流转总距离汇总
     *
     * @param id 工单工艺流程流转总距离汇总主键
     * @return 工单工艺流程流转总距离汇总
     */
    @Override
    public WorkorderDistanceSummary selectWorkorderDistanceSummaryById(Long id) {
        return workorderDistanceSummaryMapper.selectWorkorderDistanceSummaryById(id);
    }

    /**
     * 查询工单工艺流程流转总距离汇总列表
     *
     * @param workorderDistanceSummary 工单工艺流程流转总距离汇总
     * @return 工单工艺流程流转总距离汇总
     */
    @Override
    public List<WorkorderDistanceSummary> selectWorkorderDistanceSummaryList(WorkorderDistanceSummary workorderDistanceSummary) {
        return workorderDistanceSummaryMapper.selectWorkorderDistanceSummaryList(workorderDistanceSummary);
    }

    /**
     * 新增工单工艺流程流转总距离汇总
     *
     * @param workorderDistanceSummary 工单工艺流程流转总距离汇总
     * @return 结果
     */
    @Override
    public int insertWorkorderDistanceSummary(WorkorderDistanceSummary workorderDistanceSummary) {
        return workorderDistanceSummaryMapper.insertWorkorderDistanceSummary(workorderDistanceSummary);
    }

    /**
     * 修改工单工艺流程流转总距离汇总
     *
     * @param workorderDistanceSummary 工单工艺流程流转总距离汇总
     * @return 结果
     */
    @Override
    public int updateWorkorderDistanceSummary(WorkorderDistanceSummary workorderDistanceSummary) {
        return workorderDistanceSummaryMapper.updateWorkorderDistanceSummary(workorderDistanceSummary);
    }

    /**
     * 批量删除工单工艺流程流转总距离汇总
     *
     * @param ids 需要删除的工单工艺流程流转总距离汇总主键
     * @return 结果
     */
    @Override
    public int deleteWorkorderDistanceSummaryByIds(String ids) {
        return workorderDistanceSummaryMapper.deleteWorkorderDistanceSummaryByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除工单工艺流程流转总距离汇总信息
     *
     * @param id 工单工艺流程流转总距离汇总主键
     * @return 结果
     */
    @Override
    public int deleteWorkorderDistanceSummaryById(Long id) {
        return workorderDistanceSummaryMapper.deleteWorkorderDistanceSummaryById(id);
    }

    /**
     * 更新总距离表
     */
    @Override
    public int update() {
        int oldAccount = getAccount();
        int result = workorderDistanceSummaryMapper.update();
        int newAccount = getAccount();

        ContrastTable oldContrastTable = new ContrastTable();
        oldContrastTable.setState("before");
        oldContrastTable.setValue((long) oldAccount);
        oldContrastTable.setId(1L);

        ContrastTable newContrastTable = new ContrastTable();
        newContrastTable.setState("now");
        newContrastTable.setId(2L);
        newContrastTable.setValue((long) newAccount);
        contrastTableMapper.updateContrastTable(oldContrastTable);
        contrastTableMapper.updateContrastTable(newContrastTable);
        return result;
    }

    /**
     * 获取总距离
     */
    @Override
    public int getAccount() {
        return workorderDistanceSummaryMapper.getAccount();
    }

    /**
     * 获取所有信息
     */
    @Override
    public List<WorkorderDistanceSummary> getAll() {
        return workorderDistanceSummaryMapper.getAll();
    }


}
