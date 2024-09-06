package com.acc.service.impl;

import com.acc.core.entity.WorkOrder;
import com.acc.core.text.Convert;
import com.acc.mapper.WorkOrderMapper;
import com.acc.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 生产工单Service业务层处理
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {
    @Autowired
    private WorkOrderMapper workOrderMapper;

    /**
     * 查询生产工单
     *
     * @param id 生产工单主键
     * @return 生产工单
     */
    @Override
    public WorkOrder selectWorkOrderById(Long id) {
        return workOrderMapper.selectWorkOrderById(id);
    }

    /**
     * 查询生产工单列表
     *
     * @param workOrder 生产工单
     * @return 生产工单
     */
    @Override
    public List<WorkOrder> selectWorkOrderList(WorkOrder workOrder) {
        return workOrderMapper.selectWorkOrderList(workOrder);
    }

    /**
     * 新增生产工单
     *
     * @param workOrder 生产工单
     * @return 结果
     */
    @Override
    public int insertWorkOrder(WorkOrder workOrder) {
        return workOrderMapper.insertWorkOrder(workOrder);
    }

    /**
     * 修改生产工单
     *
     * @param workOrder 生产工单
     * @return 结果
     */
    @Override
    public int updateWorkOrder(WorkOrder workOrder) {
        return workOrderMapper.updateWorkOrder(workOrder);
    }

    /**
     * 批量删除生产工单
     *
     * @param ids 需要删除的生产工单主键
     * @return 结果
     */
    @Override
    public int deleteWorkOrderByIds(String ids) {
        return workOrderMapper.deleteWorkOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除生产工单信息
     *
     * @param id 生产工单主键
     * @return 结果
     */
    @Override
    public int deleteWorkOrderById(Long id) {
        return workOrderMapper.deleteWorkOrderById(id);
    }
}
