package com.acc.controller.work;

import com.acc.core.annotation.Anonymous;
import com.acc.core.dto.ExecutingOrderDto;
import com.acc.core.entity.ExecutingOrder;
import com.acc.core.entity.WorkOrder;
import com.acc.core.entity.WorkProcedure;
import com.acc.core.exception.IllegalOrderCorrespondingQuantityException;
import com.acc.core.exception.OrderNotFoundException;
import com.acc.service.OrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SuppressWarnings("LoggingSimilarMessage")
@Slf4j
@RestController
@RequestMapping("/orders")
@Api(tags = "工单处理")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final static boolean DEBUG = false;

    private final OrderService orderService;

    @Autowired
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Anonymous
    @PostMapping("/add")
    public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrder workOrder) {
        log.info("Attempting to create a new work order: {}", workOrder);
        try {
            ArrayList<WorkOrder> workOrders = new ArrayList<>();
            workOrders.add(workOrder);
            orderService.insertWorkOrders(workOrders);
            log.info("Work order created successfully");
            return ResponseEntity.ok("Work order created successfully.");
        } catch (Exception e) {
            log.error("Error creating work order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating work order: " + e.getMessage());
        }
    }
    @Anonymous
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getWorkOrder(@PathVariable int orderId) {
        log.info("Fetching work order with orderId: {}", orderId);
        if (DEBUG) {
            System.out.println("Fetching work order with orderId: " + orderId);
        }
        try {
            WorkOrder workOrder = orderService.getWorkOrderByOrderId(orderId);
            if (DEBUG) {
                System.out.println("Work order fetched successfully");
            }
            log.info("Work order fetched successfully");
            return ResponseEntity.ok(workOrder);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            if (DEBUG) {
                System.out.println("Error fetching work order: " + e.getMessage());
            }
            log.error("Error fetching work order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Anonymous
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getWorkOrderById(@PathVariable int id) {
        log.info("Fetching work order with order's id: {}", id);
        if (DEBUG) {
            System.out.println("Fetching work order with order's id: " + id);
        }
        WorkOrder workOrder = orderService.getWorkOrderById(id);
        if (DEBUG) {
            System.out.println("Work order fetched successfully");
        }
        log.info("Work order fetched successfully");
        return ResponseEntity.ok(workOrder);
    }

    @Anonymous
    @GetMapping("/procedures/{orderId}")
    public ResponseEntity<?> getWorkProcedures(@PathVariable int orderId) {
        log.info("Fetching work procedures with orderId: {}", orderId);
        if (DEBUG) {
            System.out.println("Fetching work procedures with orderId: " + orderId);
        }
        try {
            List<WorkProcedure> workProcedure = orderService.getWorkProcedure(orderId);
            if (DEBUG) {
                System.out.println("Work procedures fetched successfully");
            }
            log.info("Work procedures fetched successfully");
            return ResponseEntity.ok(workProcedure);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            if (DEBUG) {
                System.out.println("Error fetching work procedures: " + e.getMessage());
            }
            log.error("Error fetching work procedures: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @Anonymous
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteWorkOrderByOrderId(@PathVariable int orderId) {
        log.info("Attempting to delete work order with orderId: {}", orderId);
        try {
            orderService.deleteWorkOrdersByOrderId(orderId);
            log.info("Work order deleted successfully");
            return ResponseEntity.ok("Work order deleted successfully.");
        } catch (Exception e) {
            log.error("Error deleting work order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting work order: " + e.getMessage());
        }
    }
    @Anonymous
    @GetMapping("/executing/{orderId}")
    public ResponseEntity<?> createExecutingOrder(@PathVariable int orderId) {
        log.info("Attempting to get executing order for orderId: {}", orderId);
        if (DEBUG) {
            System.out.println("Attempting to get executing order for orderId: " + orderId);
        }
        try {
            ExecutingOrder executingOrder = orderService.getExecutingOrderByOrderId(orderService.getUniqueWorkOrderId(orderId));
            ExecutingOrderDto orderDto = orderService.serializeExecutingOrder(executingOrder);
            log.info("Executing order get successfully");
            if (DEBUG) {
                System.out.println("Executing order get successfully: " + orderDto);
            }
            return ResponseEntity.ok(orderDto);
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            log.error("Error creating executing order: {}", e.getMessage());
            if (DEBUG) {
                System.out.println("Error creating executing order: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating executing order: " + e.getMessage());
        }
    }
    @Anonymous
    @GetMapping("/update/{username}/{orderId}")
    public ResponseEntity<?> updateExecutingOrder(@PathVariable int orderId, @PathVariable String username) {
        log.info("Attempting to update executing order for orderId: {}", orderId);
        try {
            orderService.updateExecutingOrderState(username, orderId);
            log.info("Executing order updated successfully");
            return ResponseEntity.ok("Executing order updated successfully.");
        } catch (OrderNotFoundException | IllegalOrderCorrespondingQuantityException e) {
            log.error("Error updating executing order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error updating executing order: " + e.getMessage());
        }
    }
    @Anonymous
    @GetMapping("/all")
    public ResponseEntity<List<ExecutingOrderDto>> getAllExecutingOrders() {
        log.info("Fetching all executing orders");
        try {
            List<ExecutingOrderDto> orders = orderService.getAllExecutingOrders().stream().map(orderService::serializeExecutingOrder).toList();
            if (orders.isEmpty()) {
                log.info("No executing orders found");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(orders);
        } catch (OrderNotFoundException e) {
            log.error("Error fetching executing orders: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Anonymous
    @GetMapping("/waiting/{workCenterCode}")
    public ResponseEntity<Integer> getWaitingOrdersCountByCenterCode(@PathVariable String workCenterCode) {
        log.info("Fetching all executing orders");
        try {
            List<ExecutingOrder> orders = orderService.getAllExecutingOrders();
            if (orders.isEmpty()) {
                log.info("No executing orders found");
                return ResponseEntity.ok(0);
            }
            // Count the number of orders where the first waiting procedure is for the specified work center
            int waitingCount = (int) orders.stream()
                    .filter(order -> order.getExecuting() == null && !order.getWaiting().isEmpty())
                    .filter(order -> order.getWaiting().peek().getWorkCenter().equals(workCenterCode))
                    .count();

            log.info("Found {} work orders waiting in work center: {}", waitingCount, workCenterCode);
            return ResponseEntity.ok(waitingCount);

        } catch (OrderNotFoundException e) {
            log.error("Error fetching executing orders: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/waiting/all")
    public ResponseEntity<Map<String, Integer>> getAllWorkCentersWithWaitingOrders() {
        log.info("Fetching all work centers with waiting work orders");
        try {
            List<ExecutingOrder> orders = orderService.getAllExecutingOrders();
            if (orders.isEmpty()) {
                log.info("No executing orders found");
                return ResponseEntity.ok(Collections.emptyMap());
            }

            // Create a map to store work center code and its waiting count
            Map<String, Integer> workCenterWaitingMap = new HashMap<>();

            // Iterate through orders, check for waiting state, and accumulate counts by work center
            orders.stream()
                    .filter(order -> order.getExecuting() == null && !order.getWaiting().isEmpty()) // Filter orders with executing = null and non-empty waiting queue
                    .forEach(order -> {
                        String workCenterCode = order.getWaiting().peek().getWorkCenter(); // Get the work center of the first waiting procedure
                        workCenterWaitingMap.merge(workCenterCode, 1, Integer::sum); // Increment the count for the work center
                    });

            log.info("Found {} work centers with waiting orders", workCenterWaitingMap.size());
            return ResponseEntity.ok(workCenterWaitingMap);
        } catch (OrderNotFoundException e) {
            log.error("Error fetching executing orders: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
