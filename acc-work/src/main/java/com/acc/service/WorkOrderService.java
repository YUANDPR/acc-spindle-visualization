package com.acc.service;

import com.acc.core.entity.WorkOrder;

import java.util.List;


/**
 * 生产工单Service接口
 */
public interface WorkOrderService {
    /**
     * 查询生产工单
     *
     * @param id 生产工单主键
     * @return 生产工单
     */
     WorkOrder selectWorkOrderById(Long id);

    /**
     * 查询生产工单列表
     *
     * @param workOrder 生产工单
     * @return 生产工单集合
     */
     List<WorkOrder> selectWorkOrderList(WorkOrder workOrder);

    /**
     * 新增生产工单
     *
     * @param workOrder 生产工单
     * @return 结果
     */
     int insertWorkOrder(WorkOrder workOrder);

    /**
     * 修改生产工单
     *
     * @param workOrder 生产工单
     * @return 结果
     */
     int updateWorkOrder(WorkOrder workOrder);

    /**
     * 批量删除生产工单
     *
     * @param ids 需要删除的生产工单主键集合
     * @return 结果
     */
     int deleteWorkOrderByIds(String ids);

    /**
     * 删除生产工单信息
     *
     * @param id 生产工单主键
     * @return 结果
     */
     int deleteWorkOrderById(Long id);
}
