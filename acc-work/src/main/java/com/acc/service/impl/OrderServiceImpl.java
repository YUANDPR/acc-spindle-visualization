package com.acc.service.impl;

import com.acc.core.dto.ExecutingOrderDto;
import com.acc.core.entity.ExecutingOrder;
import com.acc.core.entity.WorkOrder;
import com.acc.core.entity.WorkProcedure;
import com.acc.core.entity.WorkRecord;
import com.acc.core.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.core.exception.OrderNotFoundException;
import com.acc.mapper.ExecutingOrderMapper;
import com.acc.mapper.OrderMapper;
import com.acc.mapper.WorkProcedureMapper;
import com.acc.service.OrderService;
import com.acc.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final static boolean DEBUG = false;
    private final RecordService recordService;
    private final WorkProcedureMapper workProcedureMapper;
    private final ExecutingOrderMapper executingOrderMapper;
    private final OrderMapper workOrderMapper;

    @Autowired
    OrderServiceImpl(WorkProcedureMapper workProcedureMapper, ExecutingOrderMapper executingOrderMapper, OrderMapper workOrderMapper, RecordServiceImpl recordServiceImpl, RecordService recordService) {
        this.workProcedureMapper = workProcedureMapper;
        this.executingOrderMapper = executingOrderMapper;
        this.workOrderMapper = workOrderMapper;
        this.recordService = recordService;
    }

    /**
     * Retrieves the unique work order ID for a given order ID.
     * If no work order is found with the given order ID, an OrderNotFoundException is thrown.
     * If multiple work orders are found for the same order ID, an IllegalOrderCorrespondingQuantityException is thrown.
     * If exactly one work order is found, its ID is returned.
     * 根据给定的订单ID检索唯一的工作订单ID。
     * 如果没有找到给定订单ID的工作订单，将抛出OrderNotFoundException。
     * 如果同一个订单ID找到多个工作订单，将抛出IllegalOrderCorrespondingQuantityException。
     * 如果确切地找到一个工作订单，将返回其ID。
     *
     * @param orderId The order ID to search for.
     * @return The unique work order ID associated with the given order ID.
     * @throws OrderNotFoundException                     If no work order matches the provided order ID.
     * @throws IllegalOrderCorrespondingQuantityException If multiple work orders match the provided order ID.
     */
    @Override
    public int getUniqueWorkOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException {
        List<Integer> workOrderIds = workOrderMapper.findWorkOrderIdsByOrderId(orderId);
        if (workOrderIds.isEmpty()) {
            throw new OrderNotFoundException("No work order found for order ID: " + orderId);
        } else if (workOrderIds.size() > 1) {
            throw new IllegalOrderCorrespondingQuantityException("Multiple work orders found for order ID: " + orderId);
        }
        return workOrderIds.get(0);
    }

    /**
     * Retrieves an ExecutingOrder by its associated work order ID. If no corresponding executing order is found in the database,
     * a new ExecutingOrder object is created from the WorkOrder data.
     * This method first searches in the ExecutingOrder table. If no entry is found, it looks up the WorkOrder table
     * to build a new ExecutingOrder, which is then saved back to the database.
     * 根据其关联的工作订单ID检索执行订单。如果数据库中没有找到相应的执行订单，
     * 将根据工作订单数据创建一个新的执行订单对象。
     * 该方法首先在执行订单表中搜索。如果没有找到记录，它将查找工作订单表
     * 来构建一个新的执行订单，然后将其保存回数据库。
     *
     * @param id The ID of the work order to search for, note this is not the orderId but the unique database ID.
     * @return An ExecutingOrder object corresponding to the specified work order ID.
     * @throws IllegalOrderCorrespondingQuantityException If more than one executing order is found for a single orderId, indicating data corruption.
     * @throws OrderNotFoundException                     If no work order is found with the specified ID in either the ExecutingOrder or WorkOrder tables.
     * @apiNote 注意这个id是对象id 而不是工单号
     */
    @Override
    public ExecutingOrder getExecutingOrderByOrderId(int id) throws IllegalOrderCorrespondingQuantityException, OrderNotFoundException {
        List<ExecutingOrderDto> byOrderId = executingOrderMapper.getByOrderId(id);
        if (byOrderId.size() > 1) {
            throw new IllegalOrderCorrespondingQuantityException("在查找执行订单时找到多个，怀疑数据被污染！");
        }
        if (byOrderId.isEmpty()) {
            WorkOrder byId = workOrderMapper.getById(id);
            if (byId != null) {
                ExecutingOrder build = build(byId);
                ExecutingOrderDto executingOrderDto = serializeExecutingOrder(build);
                if (executingOrderDto.getId() == 0) {
                    executingOrderDto.generateUniqueId();
                }
                System.out.println("insert ExecutingOrderDto: " + executingOrderDto);
                executingOrderMapper.insertExecutingOrder(executingOrderDto);
                return build;
            } else throw new OrderNotFoundException("No work order found for order ID: " + id);
        }
        ExecutingOrderDto orderDto = byOrderId.get(0);
        return deserializeExecutingOrder(orderDto);
    }

    /**
     * Constructs a new ExecutingOrder object based on a given WorkOrder. This method fetches all relevant work procedures
     * for the work order, initializes them as waiting, and sets up an empty list for done procedures and null for currently executing procedure.
     * The resulting ExecutingOrder is ready to be inserted into the database or used in further processing.
     * 根据给定的工作订单构建一个新的执行订单对象。此方法获取工作订单的所有相关工作程序，
     * 将它们初始化为等待状态，并为已完成的程序设置一个空列表，当前执行的程序为null。
     * 生成的执行订单准备插入数据库或用于进一步处理。
     *
     * @param order The WorkOrder based on which the ExecutingOrder is to be constructed.
     * @return A newly created ExecutingOrder initialized with work procedures from the given WorkOrder.
     */
    @Override
    public ExecutingOrder build(WorkOrder order) {
        List<WorkProcedure> list;

        // Use streams to filter out duplicate operationIds and retain the first occurrence of each
        list = getWorkProcedures(order);  // Collect the filtered procedures back into a list

        ExecutingOrder executingOrder = new ExecutingOrder(order);
        executingOrder.setWaiting(new LinkedList<>(list));
        executingOrder.setDone(new ArrayList<>());
        executingOrder.setExecuting(null);
        executingOrder.setStateUpdateTime(LocalDateTime.now());
        return executingOrder;
    }


    /**
     * Retrieves a list of {@link WorkProcedure} objects associated with a given order ID.
     * This method first fetches a unique work order ID for the provided order ID,
     * then uses this unique ID to fetch the corresponding {@link WorkOrder}.
     * It then retrieves a list of work procedures sorted by operation ID using the material ID of the fetched order.
     * 根据给定的订单号检索与之关联的{@link WorkProcedure}对象列表。
     * 此方法首先为提供的订单号获取一个唯一的工作订单ID，
     * 然后使用此唯一ID获取相应的{@link WorkOrder}。
     * 接着，它将使用获取的订单的材料ID来检索按操作ID排序的工作程序列表。
     *
     * @param orderId the ID of the order for which work procedures are to be fetched
     * @return a list of {@link WorkProcedure} objects sorted by operation ID
     * @throws OrderNotFoundException                     if the order with the specified ID does not exist
     * @throws IllegalOrderCorrespondingQuantityException if the operation corresponding to the order fails due to quantity issues
     */
    @Override
    public List<WorkProcedure> getWorkProcedure(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException {
        int id = getUniqueWorkOrderId(orderId);
        WorkOrder order = workOrderMapper.getById(id);
        return getWorkProcedures(order);
    }

    /**
     * Deserializes an {@link ExecutingOrderDto} into an {@link ExecutingOrder} entity.
     * This method retrieves a work order from the database using the provided DTO, checks if the work order exists,
     * and organizes the work procedures based on their execution status into executing, waiting, or done categories.
     * 反序列化一个ExecutingOrderDto到ExecutingOrder实体。
     * 此方法使用提供的DTO从数据库检索工作订单，检查工作订单是否存在，
     * 并根据执行状态将工作程序组织到执行中、等待或完成的类别中。
     *
     * @param dto 包含执行订单序列化数据的数据传输对象。the data transfer object containing the serialized data of an executing order.
     * @return 表示反序列化实体的ExecutingOrder实例。an {@link ExecutingOrder} instance representing the deserialized entity.
     * @throws OrderNotFoundException 如果根据ExecutingOrderDto中的getOrderId()找不到工作订单。 if the work order referred to by {@link ExecutingOrderDto#getOrderId()} does not exist.
     * @implNote This method fetches the work order and work procedures based on IDs provided in the DTO.
     * It then classifies procedures into executing, waiting, or done lists based on the execution status and
     * the executing procedure ID from the DTO. This method also handles conversion of update time from epoch milliseconds
     * to {@link LocalDateTime}.此方法根据DTO提供的ID获取工作订单和工作程序。
     * 然后它根据执行状态和DTO中的正在执行程序ID将程序分类到执行中、等待或完成列表。
     * 此方法还处理将更新时间从epoch毫秒转换为LocalDateTime。
     */
    @Override
    public ExecutingOrder deserializeExecutingOrder(ExecutingOrderDto dto) throws OrderNotFoundException {
        WorkOrder order = workOrderMapper.getById(dto.getOrderId());
        if (order == null) {
            throw new OrderNotFoundException("No work order found for order ID: " + dto.getOrderId());
        }
        ExecutingOrder executingOrder = new ExecutingOrder(order);
        executingOrder.setId(dto.getId());
        if (dto.getUpdate_time() == null) executingOrder.setStateUpdateTime(LocalDateTime.now());
        else
            executingOrder.setStateUpdateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getUpdate_time()), ZoneId.systemDefault()));
        List<WorkProcedure> list = getWorkProcedures(order);

        Queue<WorkProcedure> waitingQueue = new LinkedList<>();
        List<WorkProcedure> done = new ArrayList<>();
        boolean flag = false;
        if (dto.isExecuting()) {
            // * * . - - 正在执行，done不包括executing
            for (WorkProcedure workProcedure : list) {
                if (workProcedure.getId() == dto.getExecutingProcedureId() && !flag) {
                    flag = true;
                    executingOrder.setExecuting(workProcedure);
                    continue;
                }
                if (flag) {
                    waitingQueue.add(workProcedure);
                } else {
                    done.add(workProcedure);
                }
            }
        } else {
            // * * * - - 不正在执行，done包括executing
            if (dto.getExecutingProcedureId() == 0) {
                waitingQueue.addAll(list);
            } else for (WorkProcedure workProcedure : list) {
                if (workProcedure.getId() == dto.getExecutingProcedureId() && !flag) {
                    flag = true;
                    done.add(workProcedure);
                    continue;
                }
                if (flag) {
                    waitingQueue.add(workProcedure);
                } else {
                    done.add(workProcedure);
                }
            }
            executingOrder.setExecuting(null);
        }
        executingOrder.setWaiting(waitingQueue);
        executingOrder.setDone(done);
        return executingOrder;
    }


    /**
     * 获取一个订单的的所有无重复步骤的工序。
     * fetch all procedures of an order.
     *
     * @apiNote 所有需要获取工序的地方都应该使用这个方法而不是
     * workProcedureMapper.findByMaterialIdOrderByOperationId(order.getMaterialId())
     * 此方法仅限于本方法使用。
     *
     * @param order 要查询的工单。the order to fetch procedures.
     * @return 返回该order的所有无重复步骤的工序。a List of {@link WorkProcedure}.
     */
    private List<WorkProcedure> getWorkProcedures(WorkOrder order) {
        List<WorkProcedure> list = workProcedureMapper.findByMaterialIdOrderByOperationId(order.getMaterialId());

        // Use streams to filter out duplicate operationIds and retain the first occurrence of each
        list = list.stream()
                .collect(Collectors.toMap(
                        WorkProcedure::getOperationId,  // Use operationId as the key
                        procedure -> procedure,         // Keep the entire WorkProcedure as the value
                        (first, second) -> first        // If a duplicate operationId is found, keep the first one
                ))
                .values()
                .stream()
                .sorted(Comparator.comparingInt(WorkProcedure::getOperationId))
                .toList();      // Collect the filtered procedures back into a list
        return list;
    }

    /**
     * Serializes an {@link ExecutingOrder} entity into an {@link ExecutingOrderDto} data transfer object.
     * This method captures the essential fields of an executing order, such as order ID, the current executing
     * procedure ID, and the execution state. It converts the state update time from {@link LocalDateTime} to epoch milliseconds.
     * 将ExecutingOrder实体序列化为ExecutingOrderDto数据传输对象。
     * 此方法捕获执行订单的关键字段，如订单ID、当前执行程序ID和执行状态。
     * 它将状态更新时间从LocalDateTime转换为epoch毫秒。
     *
     * @param executingOrder 要序列化的ExecutingOrder实体。the {@link ExecutingOrder} entity to be serialized.
     * @return 包含序列化数据的ExecutingOrderDto。an {@link ExecutingOrderDto} containing the serialized data.
     * @implNote This method sets the order ID, update time in milliseconds, execution state, and currently
     * executing procedure ID based on the current state of the {@link ExecutingOrder} entity. It assumes that
     * the lists of done and waiting procedures are managed elsewhere or are not needed for the specific use case
     * of the serialized DTO.此方法根据ExecutingOrder实体的当前状态设置订单ID、更新时间（毫秒）、执行状态和当前执行程序ID。
     * 假设已完成和等待程序列表在其他地方管理或对特定用例的序列化DTO不是必需的。
     */
    @Override
    public ExecutingOrderDto serializeExecutingOrder(ExecutingOrder executingOrder) {
        ExecutingOrderDto dto = new ExecutingOrderDto();
        if (executingOrder.getId() != 0) {
            dto.setId(executingOrder.getId());
        } else dto.generateUniqueId();
        dto.setOrderId(executingOrder.getOrder().getId());
        dto.setUpdate_time(executingOrder.getStateUpdateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        WorkProcedure currentExecuting = executingOrder.getExecuting();
        if (currentExecuting != null) {
            log.debug("current executing: {}", currentExecuting.getDescription());
            dto.setExecuting(true);
            dto.setExecutingProcedureId(currentExecuting.getId());
        } else {
            dto.setExecuting(false);
            if (executingOrder.getDone().isEmpty()) {
                dto.setExecutingProcedureId(0);
            } else {
                List<WorkProcedure> done = executingOrder.getDone();
                dto.setExecutingProcedureId(done.get(done.size() - 1).getId());
            }
        }
        return dto;
    }

    /**
     * Updates the state of an executing order and persists the changes.
     * This method updates the state based on whether there is currently an executing task.
     * If there is, it moves the task to done and clears the executing task. If there is not,
     * it assigns the next waiting task as executing.
     * 更新执行订单的状态并持久化更改。
     * 此方法根据是否有正在执行的任务更新状态。
     * 如果有，则将任务移至完成并清除正在执行的任务。如果没有，则将下一个等待任务设置为执行。
     *
     * @param orderId the ID of the order to update
     * @throws OrderNotFoundException                     if there is an issue accessing the data
     * @throws IllegalOrderCorrespondingQuantityException if error when get many response getting order
     */
    @Override
    public void updateExecutingOrderState(String username, int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException {
        int id = getUniqueWorkOrderId(orderId);
        ExecutingOrder executingOrder = getExecutingOrderByOrderId(id);
        if (executingOrder == null) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        } else System.out.println("ExecutingOrder found with ID: " + orderId);
        Pair<Integer, Boolean> update = executingOrder.update();
        ExecutingOrderDto executingOrderDto = serializeExecutingOrder(executingOrder);
        log.info("update ExecutingOrderDto: {}", executingOrderDto);
        executingOrderMapper.updateExecutingOrder(executingOrderDto);
        if (DEBUG) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("new ExecutingOrderDto: " + getExecutingOrderDtoByOrderId(id));
        }
        recordService.addWorkRecord(new WorkRecord(
                id,
                update.getFirst(),
                username,
                LocalDateTime.now(),
                update.getSecond()));
    }

    /**
     * Retrieves a WorkOrder by its order ID by first ensuring that exactly one WorkOrder exists for the given order ID.
     * 通过工单号检索工单
     *
     * @param orderId The order ID for the WorkOrder.
     * @return The WorkOrder associated with the specified orderId.
     * @throws OrderNotFoundException                     If no WorkOrder is found or if multiple WorkOrders are found.
     * @throws IllegalOrderCorrespondingQuantityException If more than one WorkOrder is found for the same orderId.
     */
    @Override
    public WorkOrder getWorkOrderByOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException {
        int workOrderId = getUniqueWorkOrderId(orderId); // This will throw the necessary exceptions if conditions aren't met
        return workOrderMapper.getById(workOrderId);
    }

    /**
     * Retrieves a WorkOrder by its ID in database
     * 通过工单的数据库id检索工单
     *
     * @param id The order ID for the WorkOrder.
     * @return The WorkOrder associated with the specified orderId.
     */
    @Override
    public WorkOrder getWorkOrderById(int id) {
        return workOrderMapper.getById(id);
    }

    /**
     * Retrieves a ExecutingOrderDto by its order ID by first ensuring that exactly one WorkOrder exists for the given order ID.
     * 通过工单号检索执行工单，返回DTO
     *
     * @param orderId The order ID for the WorkOrder.
     * @return The WorkOrder associated with the specified orderId.
     * @throws OrderNotFoundException                     If no WorkOrder is found or if multiple WorkOrders are found.
     * @throws IllegalOrderCorrespondingQuantityException If more than one WorkOrder is found for the same orderId.
     */
    @Override
    public ExecutingOrderDto getExecutingOrderDtoByOrderId(int orderId) throws OrderNotFoundException, IllegalOrderCorrespondingQuantityException {
        List<ExecutingOrderDto> byOrderId = executingOrderMapper.getByOrderId(orderId);
        if (byOrderId.isEmpty()) {
            throw new OrderNotFoundException("No executing order found for order ID: " + orderId);
        }
        if (byOrderId.size() != 1) {
            throw new IllegalOrderCorrespondingQuantityException("more than 1 executing orders found for order ID: " + orderId);
        }
        return byOrderId.get(0);
    }


    /**
     * Inserts a list of WorkOrders into the database.
     * 列表插入工单
     *
     * @param workOrders A list of WorkOrders to be inserted.
     */
    @Override
    public void insertWorkOrders(List<WorkOrder> workOrders) {
        for (WorkOrder workOrder : workOrders) {
            workOrderMapper.insertWorkOrder(workOrder);
        }
    }

    /**
     * Deletes WorkOrder by orderId. Also deletes Executing Order with
     * the same orderId
     * 通过orderId删除工作订单。也会删除具有相同orderId的执行订单。
     *
     * @param orderId The order ID of the WorkOrders to be deleted.
     */
    @Override
    public void deleteWorkOrdersByOrderId(int orderId) {
        workOrderMapper.deleteWorkOrdersByOrderId(orderId);
        executingOrderMapper.deleteExecutingOrderByOrderId(orderId);
    }

    /**
     * Deletes a WorkOrder by id. Also deletes Executing Order with
     * the same orderId
     * 通过id删除工作订单。也会删除具有相同orderId的执行订单。
     *
     * @param id The ID of the WorkOrder to be deleted.
     */
    @Override
    public void deleteWorkOrderById(int id) {
        Integer orderId = workOrderMapper.getById(id).getOrderId();
        workOrderMapper.deleteWorkOrder(id);
        executingOrderMapper.deleteExecutingOrderByOrderId(orderId);
    }


    /**
     * Retrieves all ExecutingOrder entities from the database.
     * This method fetches all ExecutingOrderDto objects, deserializes each one into ExecutingOrder entities,
     * and returns a list of these entities. It uses the deserializeExecutingOrder method to convert each DTO into an entity.
     * 从数据库中检索所有执行订单实体。
     * 此方法获取所有ExecutingOrderDto对象，将每个对象反序列化为ExecutingOrder实体，并返回这些实体的列表。
     * 它使用deserializeExecutingOrder方法将每个DTO转换为实体。
     *
     * @return 返回所有执行订单实体的列表。A list of all ExecutingOrder entities.
     * @throws OrderNotFoundException 如果在获取过程中出现错误，例如当没有找到订单时。if there is an error during the fetching process, such as when no orders are found.
     */
    @Override
    public List<ExecutingOrder> getAllExecutingOrders() throws OrderNotFoundException {
        List<ExecutingOrderDto> allExecutingOrders = executingOrderMapper.findAllExecutingOrders();
        if (allExecutingOrders.isEmpty()) {
            throw new OrderNotFoundException("No executing orders found in the database.");
        }
        List<ExecutingOrder> executingOrders = new ArrayList<>();
        for (ExecutingOrderDto executingOrderDto : allExecutingOrders) {
            executingOrders.add(deserializeExecutingOrder(executingOrderDto));
        }
        return executingOrders;
    }

}
