package com.acc.mapper;

import com.acc.core.entity.WorkOrder;

import java.util.List;


/**
 * 生产工单Mapper接口
 */
public interface WorkOrderMapper {
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
     * 查询等待加工生产工单列表
     *
     * @return 生产工单集合
     */
    List<WorkOrder> selectNewWorkOrderList();

    /**
     * 查询等待加工生产工单总数
     *
     * @return 生产工单集合
     */
    int selectWorkOrderNumber();

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
     * 删除生产工单
     *
     * @param id 生产工单主键
     * @return 结果
     */
    int deleteWorkOrderById(Long id);

    /**
     * 批量删除生产工单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteWorkOrderByIds(String[] ids);

    /**
     * 查询以完成订单数
     *
     * @return 结果
     */
    int selectFinishWorkOrderNumber();
}
