package com.acc.controller.work;

import com.acc.core.dto.ExecutingOrderDto;
import com.acc.core.entity.ExecutingOrder;
import com.acc.core.entity.WorkOrder;
import com.acc.core.entity.WorkProcedure;
import com.acc.core.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.core.exception.OrderNotFoundException;
import com.acc.service.OrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/orders")
@Api(tags = "工单处理")
@CrossOrigin(origins = "*")
@RequiresPermissions("sss")
public class OrderController {

    private final static boolean DEBUG = false;

    private final OrderService orderService;

    @Autowired
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private static Map<String, Integer> getTestStringIntegerMap() {
        Map<String, Integer> workCenterWaitingMap = new HashMap<>();
        workCenterWaitingMap.put("test_a", 1);
        workCenterWaitingMap.put("test_b", 2);
        workCenterWaitingMap.put("test_c", 3);
        workCenterWaitingMap.put("test_d", 4);
        workCenterWaitingMap.put("test_e", 5);
        workCenterWaitingMap.put("test_f", 6);
        workCenterWaitingMap.put("test_g", 7);
        workCenterWaitingMap.put("test_h", 8);
        workCenterWaitingMap.put("test_i", 9);
        workCenterWaitingMap.put("test_j", 10);
        workCenterWaitingMap.put("test_k", 11);
        return workCenterWaitingMap;
    }

    /**
     * 创建新的工单
     *
     * @param workOrder 通过请求体传入的工单对象
     * @return 返回创建工单的结果及相应状态码
     */
    @RequiresPermissions("work:order:add")
    @PostMapping("/add")
    public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrder workOrder) {
        // 记录尝试创建新工单的日志信息
        log.info("Attempting to create a new work order: {}", workOrder);

        try {
            // 初始化一个工单列表，并将待创建的工单添加到列表中
            ArrayList<WorkOrder> workOrders = new ArrayList<>();
            workOrders.add(workOrder);

            // 调用业务服务插入工单列表
            orderService.insertWorkOrders(workOrders);

            // 记录成功创建工单的日志信息
            log.info("Work order created successfully");

            // 返回成功创建工单响应码及状态码
            return ResponseEntity.ok("Work order created successfully.");
        } catch (Exception e) {
            // 记录创建工单过程中的异常信息
            log.error("Error creating work order: {}", e.getMessage());

            // 返回创建工单失败的响应及错误状态码
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating work order: " + e.getMessage());
        }
    }

    /**
     * 根据订单ID获取工作订单详情
     *
     * @param orderId 订单ID
     * @return 返回工作订单信息或错误信息
     */
    @RequiresPermissions("work:order:get")
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getWorkOrder(@PathVariable int orderId) {
        // 记录获取工作订单的日志信息
        log.info("Fetching work order with orderId: {}", orderId);
        // DEBUG模式下打印获取工作订单的信息
        if (DEBUG) {
            System.out.println("Fetching work order with orderId: " + orderId);
        }
        try {
            // 通过订单ID从服务中获取工作订单
            WorkOrder workOrder = orderService.getWorkOrderByOrderId(orderId);
            // DEBUG模式下打印成功获取工作订单的信息
            if (DEBUG) {
                System.out.println("Work order fetched successfully");
            }
            // 记录成功获取工作订单的日志信息
            log.info("Work order fetched successfully");
            return ResponseEntity.ok(workOrder);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            // DEBUG模式下打印获取工作订单失败的错误信息
            if (DEBUG) {
                System.out.println("Error fetching work order: " + e.getMessage());
            }
            // 记录获取工作订单失败的错误日志
            log.error("Error fetching work order: {}", e.getMessage());
            // 返回未找到资源的HTTP状态码和错误信息
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * 根据ID获取工单信息的处理器方法.
     * <p>
     * 该方法通过HTTP GET请求，根据工单ID检索并返回特定工单的信息.
     * 它使用日志记录来跟踪工单检索操作的执行情况.
     *
     * @param id 工单的ID，从URL路径中获取.
     * @return 返回一个包含工单信息的响应实体.
     */
    @RequiresPermissions("work:order:get")
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getWorkOrderById(@PathVariable int id) {
        // 记录信息日志，指示正在检索特定ID的工单
        log.info("Fetching work order with order's id: {}", id);

        // 调试输出，如果启用了DEBUG模式，打印检索工单的信息
        if (DEBUG) {
            System.out.println("Fetching work order with order's id: " + id);
        }

        // 调用服务层方法根据ID获取工单
        WorkOrder workOrder = orderService.getWorkOrderById(id);

        // 调试输出，如果启用了DEBUG模式，打印工单获取成功的消息
        if (DEBUG) {
            System.out.println("Work order fetched successfully");
        }

        // 记录工单成功获取的信息日志
        log.info("getWorkOrderById - Work order fetched successfully");

        // 返回包含工单信息的响应实体
        return ResponseEntity.ok(workOrder);
    }

    /**
     * 根据订单ID获取工作流程信息
     * <p>
     * 此方法用于根据特定订单的ID检索其相关的工作流程信息它首先记录一个信息消息，指示正在尝试获取哪个订单的工作流程
     * 如果启用了调试模式，它还会在控制台打印相同的信息消息在尝试从订单服务中检索工作流程数据时，如果操作成功，它会记录一个成功消息并返回工作流程数据
     * 相反，如果遇到订单未找到或订单对应的数量非法异常，它会记录错误消息并返回一个不成功的HTTP状态以及错误信息
     *
     * @param orderId 订单的唯一标识符
     * @return 一个ResponseEntity对象，包含请求的状态码、头信息和工作流程数据或错误信息
     */
    @RequiresPermissions("work:procedure:get")
    @GetMapping("/procedures/{orderId}")
    public ResponseEntity<?> getWorkProcedures(@PathVariable int orderId) {
        // 记录尝试获取工作流程的日志消息
        log.info("Fetching work procedures with orderId: {}", orderId);
        // 如果调试模式启用，打印尝试获取工作流程的信息
        if (DEBUG) {
            System.out.println("Fetching work procedures with orderId: " + orderId);
        }
        try {
            // 尝试根据订单ID获取工作流程
            List<WorkProcedure> workProcedure = orderService.getWorkProcedure(orderId);
            // 如果调试模式启用，打印成功获取工作流程的信息
            if (DEBUG) {
                System.out.println("Work procedures fetched successfully");
            }
            // 记录成功获取工作流程的日志消息
            log.info("Work procedures fetched successfully");
            // 返回成功状态和工作流程数据
            return ResponseEntity.ok(workProcedure);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            // 如果调试模式启用，打印错误信息
            if (DEBUG) {
                System.out.println("Error fetching work procedures: " + e.getMessage());
            }
            // 记录错误日志消息
            log.error("Error fetching work procedures: {}", e.getMessage());
            // 返回请求不成功的HTTP状态和错误信息
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 通过订单ID删除工作订单。
     *
     * @param orderId 工作订单的ID。
     * @return 返回删除结果的响应实体。
     */
    @RequiresPermissions("work:order:delete")
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteWorkOrderByOrderId(@PathVariable int orderId) {
        // 记录尝试删除的工作订单的ID
        log.info("Attempting to delete work order with orderId: {}", orderId);
        try {
            // 调用服务层方法，根据订单ID删除工作订单
            orderService.deleteWorkOrdersByOrderId(orderId);
            // 记录工作订单删除成功的信息
            log.info("Work order deleted successfully");
            // 返回删除成功的响应
            return ResponseEntity.ok("Work order deleted successfully.");
        } catch (Exception e) {
            // 记录删除工作订单时的错误信息
            log.error("Error deleting work order: {}", e.getMessage());
            // 返回删除出错的响应，包含错误信息
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting work order: " + e.getMessage());
        }
    }

    /**
     * 通过订单ID获取正在执行的订单详情
     *
     * @param orderId 订单ID
     * @return 返回订单详情对象或错误信息
     */
    @RequiresPermissions("work:executing:get")
    @GetMapping("/executing/{orderId}")
    public ResponseEntity<?> createExecutingOrder(@PathVariable int orderId) {
        // 记录尝试获取正在执行的订单的日志
        log.info("Attempting to get executing order for orderId: {}", orderId);
        if (DEBUG) {
            // 如果处于调试模式，输出尝试获取正在执行的订单的信息
            System.out.println("Attempting to get executing order for orderId: " + orderId);
        }
        try {
            // 通过订单服务根据订单ID获取正在执行的订单
            ExecutingOrder executingOrder = orderService.getExecutingOrderByOrderId(orderService.getUniqueWorkOrderId(orderId));
            // 将正在执行的订单对象序列化为DTO
            ExecutingOrderDto orderDto = orderService.serializeExecutingOrder(executingOrder);
            // 记录成功获取正在执行的订单的日志
            log.info("Executing order get successfully");
            if (DEBUG) {
                // 如果处于调试模式，输出成功获取正在执行的订单的信息
                System.out.println("Executing order get successfully: " + orderDto);
            }
            // 返回订单DTO及OK状态
            return ResponseEntity.ok(orderDto);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            // 记录创建正在执行的订单时的错误日志
            log.error("Error creating executing order: {}", e.getMessage());
            if (DEBUG) {
                // 如果处于调试模式，输出创建正在执行的订单时的错误信息
                System.out.println("Error creating executing order: " + e.getMessage());
            }
            // 返回错误状态和错误信息
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating executing order: " + e.getMessage());
        }
    }

    /**
     * 通过REST API更新指定用户的执行中订单状态
     *
     * @param orderId  订单ID
     * @param username 用户名
     * @return 更新成功或失败的响应
     */
    @RequiresPermissions("work:executing:update")
    @GetMapping("/update/{username}/{orderId}")
    public ResponseEntity<?> updateExecutingOrder(@PathVariable int orderId, @PathVariable String username) {
        // 记录更新执行中订单状态的尝试
        log.info("Attempting to update executing order for orderId: {}", orderId);
        try {
            // 调用服务层方法更新执行中订单的状态
            orderService.updateExecutingOrderState(username, orderId);
            // 记录成功更新执行中订单的状态
            log.info("Executing order updated successfully");
            // 返回更新成功的响应
            return ResponseEntity.ok("Executing order updated successfully.");
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            // 记录更新执行中订单状态时出现的错误
            log.error("Error updating executing order: {}", e.getMessage());
            // 返回更新失败的响应，包含错误信息
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error updating executing order: " + e.getMessage());
        }
    }

    /**
     * 响应GET请求，获取所有正在执行的订单
     *
     * @return 包含所有正在执行的订单的响应实体，或者在没有订单时返回204 NO CONTENT，
     * 在发生错误时返回404 NOT FOUND或500 INTERNAL SERVER ERROR
     */
    @RequiresPermissions("work:executing:get")
    @GetMapping("/all")
    public ResponseEntity<List<ExecutingOrderDto>> getAllExecutingOrders() {
        log.info("Fetching all executing orders");
        try {
            // 获取所有正在执行的订单，并将其转换为序列化的DTO形式
            List<ExecutingOrderDto> orders = orderService.getAllExecutingOrders().stream()
                    .map(orderService::serializeExecutingOrder).toList();
            if (orders.isEmpty()) {
                log.info("getAllExecutingOrders - No executing orders found");
                // 当没有找到正在执行的订单时，返回204 NO CONTENT响应
                return ResponseEntity.noContent().build();
            }
            // 返回包含所有正在执行订单的响应实体
            return ResponseEntity.ok(orders);
        } catch (OrderNotFoundException e) {
            log.error("getAllExecutingOrders - Error fetching executing orders: {}", e.getMessage());
            // 当发生特定订单未找到异常时，返回404 NOT FOUND响应
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("getAllExecutingOrders - Internal server error: {}", e.getMessage());
            // 当发生内部服务器错误时，返回500 INTERNAL SERVER ERROR响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 根据工作中心代码查询待处理订单数量
     *
     * @param workCenterCode 工作中心的代码
     * @return 包含待处理订单数量的响应实体
     */
    @RequiresPermissions("work:order:get")
    @GetMapping("/waiting/{workCenterCode}")
    public ResponseEntity<Integer> getWaitingOrdersCountByCenterCode(@PathVariable String workCenterCode) {
        log.info("获取所有正在执行的订单");

        if (DEBUG) return ResponseEntity.ok(getTestStringIntegerMap().getOrDefault(workCenterCode, 5));
        try {
            List<ExecutingOrder> orders = orderService.getAllExecutingOrders();

            if (orders.isEmpty()) {
                log.info("未找到正在执行的订单");
                return ResponseEntity.ok(0);
            }

            // 统计指定工作中心的第一个等待工序的订单数量
            int waitingCount = (int) orders.stream()
                    .filter(order -> order.getExecuting() == null && !order.getWaiting().isEmpty())
                    .filter(order -> order.getWaiting().peek().getWorkCenter().equals(workCenterCode))
                    .count();

            log.info("在工作中心 {} 中找到 {} 个等待中的工单", workCenterCode, waitingCount);
            return ResponseEntity.ok(waitingCount);

        } catch (OrderNotFoundException e) {
            log.error("获取正在执行的订单时出错: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("内部服务器错误: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取所有有待处理订单的工作中心
     *
     * @return 包含所有工作中心及其待处理订单数的映射的响应实体
     */
    @RequiresPermissions("work:order:get")
    @GetMapping("/waiting/all")
    public ResponseEntity<Map<String, Integer>> getAllWorkCentersWithWaitingOrders() {
        if (DEBUG) {
            return ResponseEntity.ok(getTestStringIntegerMap());
        }

        log.info("Fetching all work centers with waiting work orders");
        try {
            // 获取所有正在执行的订单
            List<ExecutingOrder> orders = orderService.getAllExecutingOrders();
            if (orders.isEmpty()) {
                // 如果没有正在执行的订单，记录信息并返回空映射
                log.info("No executing orders found");
                return ResponseEntity.ok(Collections.emptyMap());
            }

            // 创建映射以存储工作中心代码及其待处理计数
            Map<String, Integer> workCenterWaitingMap = new HashMap<>();

            // 遍历订单，检查待处理状态，并按工作中心累积计数
            orders.stream()
                    .filter(order -> order.getExecuting() == null && !order.getWaiting().isEmpty()) // 过滤执行为空且待处理队列非空的订单
                    .forEach(order -> {
                        // 获取第一个待处理工序的工作中心
                        String workCenterCode = order.getWaiting().peek().getWorkCenter();
                        // 对应工作中心的待处理计数加一
                        workCenterWaitingMap.merge(workCenterCode, 1, Integer::sum);
                    });

            // 记录找到的工作中心数量
            log.info("Found {} work centers with waiting orders", workCenterWaitingMap.size());
            // 返回包含工作中心及其待处理订单数的映射
            return ResponseEntity.ok(workCenterWaitingMap);
        } catch (OrderNotFoundException e) {
            // 如果未找到执行订单，记录错误并返回未找到资源的响应
            log.error("Error fetching executing orders: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 对于其他异常，记录错误并返回内部服务器错误的响应
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
