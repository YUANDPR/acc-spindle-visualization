package com.acc.work.service;

import com.acc.dto.ExecutingOrderDto;
import com.acc.entity.work.ExecutingOrder;
import com.acc.entity.work.WorkOrder;
import com.acc.entity.work.WorkProcedure;
import com.acc.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.exception.OrderNotFoundException;

import java.util.List;

/**
 * @Package com.acc.service.impl
 * @ClassName OrderService
 * @Description
 * @Author YUAND
 * @Date 2024/8/21 12:25
 * @Version 1.0
 */
public interface OrderService {

    int getUniqueWorkOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    ExecutingOrder getExecutingOrderByOrderId(int id) throws IllegalOrderCorrespondingQuantityException, OrderNotFoundException;

    ExecutingOrder build(WorkOrder order);

    List<WorkProcedure> getWorkProcedure(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    ExecutingOrder deserializeExecutingOrder(ExecutingOrderDto dto) throws OrderNotFoundException;

    ExecutingOrderDto serializeExecutingOrder(ExecutingOrder executingOrder);

    void updateExecutingOrderState(String username, int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    WorkOrder getWorkOrderByOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    ExecutingOrderDto getExecutingOrderDtoByOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException;

    void insertWorkOrders(List<WorkOrder> workOrders);

    void deleteWorkOrdersByOrderId(int orderId);

    void deleteWorkOrderById(int id);

    List<ExecutingOrder> getAllExecutingOrders() throws OrderNotFoundException;
}
