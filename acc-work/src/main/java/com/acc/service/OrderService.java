package com.acc.service;


import com.acc.core.dto.ExecutingOrderDto;
import com.acc.core.entity.ExecutingOrder;
import com.acc.core.entity.WorkOrder;
import com.acc.core.entity.WorkProcedure;
import com.acc.core.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.core.exception.OrderNotFoundException;

import java.util.List;

/**
 * 订单服务接口，提供与订单相关的操作
 */
public interface OrderService {

    /**
     * 获取唯一的工单ID
     *
     * @param orderId 订单ID
     * @return 工单ID
     * @throws OrderNotFoundException                     当订单不存在时抛出异常
     * @throws IllegalOrderCorrespondingQuantityException 当订单对应数量不合法时抛出异常
     */
    int getUniqueWorkOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    /**
     * 根据订单ID获取执行中的订单
     *
     * @param id 订单ID
     * @return 执行中的订单
     * @throws IllegalOrderCorrespondingQuantityException 当订单对应数量不合法时抛出异常
     * @throws OrderNotFoundException                     当订单不存在时抛出异常
     */
    ExecutingOrder getExecutingOrderByOrderId(int id) throws IllegalOrderCorrespondingQuantityException, OrderNotFoundException;

    /**
     * 构建一个执行中的订单
     *
     * @param order 工单
     * @return 执行中的订单
     */
    ExecutingOrder build(WorkOrder order);

    /**
     * 根据订单ID获取工作流程
     *
     * @param orderId 订单ID
     * @return 工作流程列表
     * @throws OrderNotFoundException                     当订单不存在时抛出异常
     * @throws IllegalOrderCorrespondingQuantityException 当订单对应数量不合法时抛出异常
     */
    List<WorkProcedure> getWorkProcedure(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    /**
     * 从DTO对象反序列化执行中的订单
     *
     * @param dto 执行订单DTO对象
     * @return 执行中的订单
     * @throws OrderNotFoundException 当订单不存在时抛出异常
     */
    ExecutingOrder deserializeExecutingOrder(ExecutingOrderDto dto) throws OrderNotFoundException;

    /**
     * 序列化执行中的订单为DTO对象
     *
     * @param executingOrder 执行中的订单
     * @return 执行订单DTO对象
     */
    ExecutingOrderDto serializeExecutingOrder(ExecutingOrder executingOrder);

    /**
     * 更新执行中的订单状态
     *
     * @param username 用户名
     * @param orderId  订单ID
     * @throws OrderNotFoundException                     当订单不存在时抛出异常
     * @throws IllegalOrderCorrespondingQuantityException 当订单对应数量不合法时抛出异常
     */
    void updateExecutingOrderState(String username, int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    /**
     * 根据订单ID获取工单
     *
     * @param orderId 订单ID
     * @return 工单
     * @throws OrderNotFoundException                     当订单不存在时抛出异常
     * @throws IllegalOrderCorrespondingQuantityException 当订单对应数量不合法时抛出异常
     */
    WorkOrder getWorkOrderByOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    /**
     * 根据订单ID获取执行订单DTO
     *
     * @param orderId 订单ID
     * @return 执行订单DTO
     * @throws OrderNotFoundException                     当订单不存在时抛出异常
     * @throws IllegalOrderCorrespondingQuantityException 当订单对应数量不合法时抛出异常
     */
    ExecutingOrderDto getExecutingOrderDtoByOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    /**
     * 插入工单
     *
     * @param workOrders 工单列表
     */
    void insertWorkOrders(List<WorkOrder> workOrders);

    /**
     * 根据订单ID删除工单
     *
     * @param orderId 订单ID
     */
    void deleteWorkOrdersByOrderId(int orderId);

    /**
     * 根据ID删除工单
     *
     * @param id 工单ID
     */
    void deleteWorkOrderById(int id);

    /**
     * 获取所有执行中的订单
     *
     * @return 执行中的订单列表
     * @throws OrderNotFoundException 当订单不存在时抛出异常
     */
    List<ExecutingOrder> getAllExecutingOrders() throws OrderNotFoundException;

    /**
     * 根据ID获取工单
     *
     * @param id 工单ID
     * @return 工单
     */
    WorkOrder getWorkOrderById(int id);

}

